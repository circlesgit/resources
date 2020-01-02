#!/bin/bash
#Parse the CIDR and store it into an array
IFS='/.' read -r -a array <<< "$1"
#First 3 octet
significant_bit="${array[0]}.${array[1]}.${array[2]}"
#network mask
network_mask=${array[4]}
#Find the bits which are turned off
netmask_length=$((32-$network_mask))
#Number of available hosts (2^n)
available_address=$((2**$netmask_length))
forth_octet=${array[3]}
#total number of required subnets
total_subnet=$2
#initial subnet
subnet_count=0

subnetting () {
    [  -z "$1" ] && { echo "Subnet length cannot be empty"; exit 1; }
    n=1 #initialize bits count to 1
    if [ $1 -gt 0 ]
    then
        forth_octet=$(( $forth_octet + $subnet_count ))
        current_address_count=2
        while [ $current_address_count -lt $(( $available_address /  $1 ))  ]; do
          n=$(($n+1))
          current_address_count=$(( 2 ** $n))
        done

        #Calculate total number of address for each subsequent network
        subnet_count=$(( 2 ** $n ))
        last_octet_length=$(( $last_octet_length + $subnet_count ))
        # Available addresses left to be assigned to the other subnets
        available_address=$(( $available_address - $subnet_count ))
        subnet_mask=$(( $netmask_length - $n + $network_mask ))
        #Total hosts
        total_hosts=$(( $subnet_count - 3 ))
        result="Subnet= $significant_bit.$forth_octet/$subnet_mask \
                Network=$significant_bit.$forth_octet \
                Broadcast=$significant_bit.$(( $last_octet_length - 1)) \
                Gateway= $significant_bit.$(( $forth_octet + 1 )) \
                Hosts=$total_hosts"
        echo $result
        #recursion call
        subnetting $(($1 - 1))
    fi
}
subnetting $total_subnet
