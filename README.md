# aws-demo
This project demonstrates how to deploy and run a Java service using Amazon Web Services (AWS).

# Requirements
* The deployment playbooks require credentials to be set up via environment variables. For example:
  * export AWS_ACCESS_KEY_ID=access key goes here
  * export AWS_SECRET_ACCESS_KEY=secret access key goes here
* To SSH to the EC2 instances we have to [generate an SSH key-pair]( https://help.github.com/articles/generating-ssh-keys/) and let the playbook copy the public key when setting up the VPC.
