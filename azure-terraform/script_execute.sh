
echo "Switch into Terraform AKS Cluster Set-up directory"
cd terraform-aks-setup
# make sure terraform CLI is installed
#terraform

# format the tf files
#terraform fmt

# initialize terraform Azure modules
terraform init

# validate the template
terraform validate

# plan and save the infra changes into tfplan file
terraform plan

# apply the infra changes
terraform apply --auto-approve

# delete the infra
#terraform destroy

cd ../
echo "Switch into Terraform Helm Release directory"
cd terraform-helm-deploy
# make sure terraform CLI is installed
#terraform

# format the tf files
#terraform fmt

# initialize terraform Azure modules
terraform init

# validate the template
terraform validate

# plan and save the infra changes into tfplan file
terraform plan

# apply the infra changes
terraform apply --auto-approve

# delete the infra
#terraform destroy
