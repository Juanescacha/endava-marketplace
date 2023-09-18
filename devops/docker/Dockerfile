#-----------------------------------------
# Build Server Image
#-----------------------------------------

FROM maven:latest

# Install ansible
RUN apt update 
RUN apt install -y software-properties-common 
RUN add-apt-repository --yes --update ppa:ansible/ansible 
RUN apt install ansible -y

# Install node
RUN apt install nodejs -y