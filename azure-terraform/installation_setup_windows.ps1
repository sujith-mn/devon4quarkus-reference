
echo "Install Azure cli..."
$ProgressPreference = 'SilentlyContinue'; Invoke-WebRequest -Uri https://aka.ms/installazurecliwindows -OutFile .\AzureCLI.msi; Start-Process msiexec.exe -Wait -ArgumentList '/I AzureCLI.msi /quiet'; rm .\AzureCLI.msi

echo "az version"
az --version

echo "Install Chocolatey package "
#Install Chocolatey package  before helm install
Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))
#Verify  choco installation
choco 

echo "Install Kubectl.."
Install-AzAksKubectl

#echo "Install kubectl using choco"
#choco install kubernetes-cli
#kubectl version --client



echo "Install Helm package"
#Install Helm 
choco install kubernetes-helm
#Verify helm installation
helm

echo "Install Terraform.."
. .\Install-Terraform.ps1
Install-Terraform
