# aws-demo

This project demonstrates how to deploy and run a template Java service using Amazon Web Services (AWS).

In particular, the deployment playbooks and the template service demonstrate how to:

* Create a Virtual Private Cloud (VPC).
* Maintain a public key used for deployment.
* Create your own Amazon Machine Image (AMI) that is pre-installed with the service and its dependencies.
* Create an Elastic Load Balancer (ELB) that will route traffic to service instances across Availability Zones.
* Create a Launch Configuration and a matching Auto-scaling Group (ASG) that will automatically scale based on load.

## Requirements

The deployment playbooks require credentials to be set up via environment variables. For example:

  * `export AWS_ACCESS_KEY_ID=my-access-key`
  * `export AWS_SECRET_ACCESS_KEY=my-secret-key`

Ansible 2.0 is required due to the inclusion of playbooks using newer AWS modules. In addition, you would need to copy the plugins in filter_plugins to ~/.ansible/plugins/filter_plugins/.

To build and deploy the service package you could use the build script or set up a CI job. A few dependencies are required for the build script: maven, fpm, createrepo, and s3cmd.

## Create a Virtual Private Cloud (VPC)

The **aws-vpc.yml** playbook sets up a total of four subnets. Two subnets belong to the web subnet group and span two different availability zones. The same applies for the db subnet group.

The VPC is using the following address range 10.0.0.0/16 and includes the following subnets:

* 10.0.0.0/24 **web-subnet-1a** / availability zone: 'eu-west-1a'
* 10.0.1.0/24 **web-subnet-1b** / availability zone: 'eu-west-1b'
* 10.0.2.0/24 **db-subnet-1b** / availability zone: 'eu-west-1a'
* 10.0.3.0/24 **db-subnet-1b** / availability zone: 'eu-west-1b'

The VPC playbook can be run standalone, as follows:

`$ ansible-playbook aws-vpc.yml -i inventories/dev`

Several other playbooks, such as setting-up an ELB require subnet details. These other playbooks re-run the VPC playbook to ensure the details are registers so that the VPC information can be used in later roles.

## Maintain a deployment public key

In order to spawn new EC2 instances that can we can ssh to, we need to maintain a public key that we use for deployment.

[Generate an SSH key-pair]( https://help.github.com/articles/generating-ssh-keys/) and let the playbook copy the public key as follows:

`$ ansible-playbook aws-public-keys.yml -i inventories/dev`

The public key should then be specified by name when spawning a new EC2 instance. The instance will be set up with the public key as one of the authorized keys allowed to log in as **ec2-user**.

## Create an IAM role

IAM roles can be assigned to EC2 instances. When an EC2 instance assumes a particular IAM role, it could, for example, make API requests.

This allows the developer to avoid maintaining and distributing particular AWS credentials to each instance.

`$ ansible-playbook aws-iam-role.yml -i inventories/dev`

## Creating an Amazon Machine Image (AMI)

Several base images exist that can be used to spawn new EC2 instances (based on Amazon Linux, CentOS, Ubuntu, etc).

When spawning a new instance we could provision the instance completely on-demand by running a a provisioning script. Roles such as aws-demo-common and aws-demo-service are used to configure the instance.
However, in order to save time and ensure our newly spawned instances are completely identical, we will will provision a template instance and save an image (AMI) that we will use later.
This process is known as "baking an image".

The following playbook creates a new EC2 instance only to configure it and save the template image. The instance is then shut down.

Should the launch configuration be created here?? Would be better to be able to discover the AMI in a separate playbook.

`$ ansible-playbook aws-bake-image.yml -i inventories/dev`

## Create an Elastic Load Balancer (ELB)

An ELB routes traffic between different EC2 instances that span Availability Zones (AZs).

In this example, we create an Internet-facing ELB with a corresponding Security Group that opens port 80 and proxies it to the instance's application port (8080).

In addition, we set up health-checks for each EC2 instances. By using the running service's admin port (8081) and designated health-check endpoint.

`$ ansible-playbook aws-elb.yml -i inventories/dev`

## Create an Auto Scaling Group (ASG)

An ASG facilitates creation of EC2 instances spanning Availability Zones and ensures that the desired instance count is available.

The group created by this playbook is associated with a corresponding ELB which routes traffic to each instance within the group.  

When scaling policies are defined, the number of instances can change based on demand.

`$ ansible-playbook aws-asg.yml -i inventories/dev`