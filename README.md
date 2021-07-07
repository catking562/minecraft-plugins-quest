# minecraft-plugins-quest
# made.by taewookim562 (in korea)
# lenguage = korean

name: quest
version: 0.1
main: taewookim562.main
description: make a custom quest in minecraft!
authors: taewookim562
website: https://github.com/catking562/minecraft-plugins-quest.git

commands:
  퀘스트:
    description: user command
    aliases: [quest]
    permission: quest.user
  퀘스트설정:
    description: you can this command that make, set, manage quest
    aliases: [questoption]
    permission: quest.admin
  퀘스트관리:
    description: manage player quest
    aliases: [questmanager]
    permission: quest.admin
    
permissions:
  quest.*:
    description: quest all permissions
    default: op
    children:
      quest.user: true
      quest.admin: true
  quest.user:
    description: quest user permissios
    default: true
  quest.admin:
    description: quest admin permissions
    default: true
    children:
      quest.user: true