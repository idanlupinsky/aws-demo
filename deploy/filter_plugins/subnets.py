def subnetworks(all_subnets, subnet_cidrs):
    return [elem for elem in all_subnets if elem['cidr'] in subnet_cidrs]

class FilterModule(object):
    ''' A filter to retrieve only particular VPC subnetworks. '''
    def filters(self):
        return {
            'subnetworks' : subnetworks,
        }