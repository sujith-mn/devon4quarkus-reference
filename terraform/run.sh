# make sure terraform CLI is installed
terraform

# initialize terraform Azure modules
terraform init

# validate the template
terraform validate

# plan and save the infra changes into tfplan file.
terraform plan

# delete the infra
#terraform destroy

# cleanup files
#rm terraform.tfstate
#rm terraform.tfstate.backup
#rm .terraform.lock.hcl
#rm -r .terraform/
