# 为什么需要版本控制

## 概述

在软件开发过程，每天都会产生新的代码，代码合并的过程中可能会出现如下问题：

- 代码被覆盖或丢失
- 代码写的不理想希望还原之前的版本
- 希望知道与之前版本的差别
- 是谁修改了代码以及为什么修改
- 发版时希望分成不同的版本(测试版、发行版等)

因此，我们希望有一种机制，能够帮助我们：

- 可以随时回滚到之前的版本
- 协同开发时不会覆盖别人的代码
- 留下修改记录，以便随时查看
- 发版时可以方便的管理不同的版本

## 什么是版本控制系统

一个标准的版本控制系统 Version Control System (VCS)，通常需要有以下功能：

- 能够创建 Repository (仓库)，用来保存代码
- 协同开发时方便将代码分发给团队成员
- 记录每次修改代码的内容、时间、原因等信息
- 能够创建 Branch (分支)，可以根据不同的场景进行开发
- 能够创建 Tag (标签)，建立项目里程碑

## 版本控制系统的发展史

版本控制系统发展至今有几种不同的模式：

### Local VCS

本地使用 `复制/粘贴` 的方式进行管理，缺点是无法团队协同开发

### Centralized VCS (Lock，悲观锁)

中央集中式版本控制系统团队共用仓库，当某人需要编辑文件时，进行锁定，以免其他人同时编辑时造成冲突。缺点是虽然避免了冲突，但不是很方便。其他人需要排队才能编辑文件，如果有人编辑了很久或是忘记解锁就会造成其他人长时间等待的情况。

### Centralized VCS (Merge，乐观锁)

中央集中式版本控制系统团队共用仓库，不采用悲观锁方式来避免冲突，而是事后发现如果别人也修改相同文件(冲突)，再进行手动修改解决。有很多 VCS 属于这种类型，如：CVS，Subversion，Perforce 等

中央集中式版本控制系统的共同问题是，做任何操作都需要和服务器同步，如果服务器宕机则会造成无法继续工作的窘迫。

### Distributed VCS

分布式版本控制系统，本地也拥有完整的代码仓库，就不会出现上述集中式管理的问题，即使没有网络，依然可以 `commit` 和看 `log`，也无需担心服务器同步问题。如：Git，Mercurial，Bazaar 等就属于分布式版本控制系统。缺点是功能比较复杂，上手需要一定的学习时间。