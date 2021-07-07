# minecraft-plugins-quest
# made.by taewookim562 (in korea)
# lenguage = korean

name: quest\n
version: 0.1\n
main: taewookim562.main\n
description: make a custom quest in minecraft!\n
authors: taewookim562\n
website: https://github.com/catking562/minecraft-plugins-quest.git\n
\n
commands:\n
  퀘스트:\n
    description: user command\n
    aliases: [quest]\n
    permission: quest.user\n
  퀘스트설정:\n
    description: you can this command that make, set, manage quest\n
    aliases: [questoption]\n
    permission: quest.admin\n
  퀘스트관리:\n
    description: manage player quest\n
    aliases: [questmanager]\n
    permission: quest.admin\n
    \n
permissions:\n
  quest.*:\n
    description: quest all permissions\n
    default: op\n
    children:\n
      quest.user: true\n
      quest.admin: true\n
  quest.user:\n
    description: quest user permissios\n
    default: true\n
  quest.admin:\n
    description: quest admin permissions\n
    default: true\n
    children:\n
      quest.user: true\n