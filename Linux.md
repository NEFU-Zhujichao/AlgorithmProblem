# Linux基本指令

> 基本指令：cd ls mkdir rmdir cp mv pwd touch rm ll ls -lh h: 文件带单位
>
> vi 命令行模式 /搜索内容  :set nu 显示行号
>
> 单行注释 #  多行注释
>
> :<<!
>
> !
>
> yy 复制一行
>
> p 粘贴一行

### 查看文件内容 cat more less head tail

- cat -n /etc/profile | more    | 管道符 将前面的内容交给more分页显示，按空格键翻下一页
- more指令 按空格键翻下一页 回车是下一行 q离开 ctrl+B 上一页 ctrl+F 下一页
- less指令 显示文件内容时，不是一次加载整个文件，而是根据显示需要加载内容，对于大型文件效率很高
- echo $PATH 输出环境变量
- '> ' 将原来文件内容覆盖  >> 追加到文件尾部
- head  -n 10 显示前10行(默认前10行)
- tail -n 10 显示后10行 -f 实时监控文件
- 文件翻页 ctrl+D ctrl+U     n,N	搜索字符串时，用n来继续搜索下一个，N来进行反向搜索下一个
### 链接文件 ln

格式：ln -s 源文件路径 软链接名称

软链接也叫符号链接(类似于windows的快捷方式)：ln的链接又软链接 和硬链接两种，软链接就是ln -s XX XX,它只会在你选定的位置上生成一个文件的镜像，不会占用磁盘空间，硬链接ln ** **,没有参数-s, 它会在你选定的位置上生成一个和源文件大小相同的文件，无论是软链接还是硬链接，文件都保持同步变化。

**踩坑**：删除软链接时要注意，建议进入到软链接的然后用 rm软链接名  删除软链接。否则可能会误删源文件！

### 查看历史指令 history

- 查看最近使用的linux指令
- history 10 显示最近使用的10个
-  !5 执行5号指令

### 日期和日历 date cal

#### date

- date 显示当前时间信息 Wed Jun  9 16:38:08 CST 2021
- date "+%Y-%m-%d" 2021-06-09
- date "+%Y-%m-%d %H:%M:%S" 2021-06-09 16:41:24
- date "+%Y-%m-%d %H:%M:%s" 2021-06-09 16:41:1623228090

- date -s  "2018-11-11 11:22:22" 设置系统时间

#### cal

- cal 显示日历
- cal 2020 显示2020年的日历

### 查找文件 find locate

- find [搜索范围] [选项]
    - -name    find /home -name hello.txt   find / -name *.java 通配符
    - -user      find /home -user root
    - -size       find / -size 20M  +20M  大于20M -20M 小于20M 20M 等于20M (M、k)
- locate
    - updatedb 创建locate数据库
    - locate hello.txt

### 过滤 grep

- | 管道符：表示将前一个命令的处理结果输出传递给后面的命令处理。 用管道符将前后两个指令连接起来。
- grep [选项] 查找内容 源文件
    - -n 显示匹配行及行号
    - -i 忽略字母大小写

### 压缩和解压

- gzip 用于压缩文件(可以多个)   	gzip *    得到 *.gz 的压缩文件 源文件不存在了
- gunzip 用于解压文件(可以多个)   gunzip *

- zip 用于压缩文件 和目录   zip [选项] XXX.zip 压缩的内容
    - -r 递归压缩 压缩整个目录     zip -r  mypackage.zip /home/  生成一个mypackage.zip 源文件还存在
- unzip
    - -d 指定解压后文件存放的目录  unzip -d /home/test mypackage.zip
- tar 打包指令  tar [选项] xxx.tar.gz
    - -f 压缩后的文件名字
    - -c 产生.tar打包文件
    - -v 显示详细信息
    - -z 同时打包压缩
    - -x 解压.tar文件
        - 压缩多个文件，将/home/a1.txt 和 /home/a2.txt 压缩成 a.tar.gz   tar -zcvf a.tar.gz a1.txt a2.txt
        - tar -zcvf myhome.tar.gz /home/
        - 解压文件 tar -zxvf a.tar.gz
        - 将myhome.tar.gz 解压到 /home/test目录下 1.先要有那个目录 2. tar -zxvf myhome.tar.gz  -C /home/test

### crond 任务调度

#### 基本语法

- crontab [选项]
    - -e 编辑crontab定时任务
    - -l 查询crontab定时任务
    - -r 删除当前用户所有的crontab定时任务
- 简单的任务直接写在crontab中，复杂的任务写在shell脚本中
    - */1 * * * * date >> /home/shell/test.txt
    - */1 * * * * /home/shell/date.sh
- cron表达式 分 时 日 月 星期

### 进程管理

####  查看进程 ps

- ps -aux
    - -a 显示当前终端所有进程信息
    - -u 以用户的格式显示进程信息
    - -x 显示后台进程运行的参数

- ps -ef 以全格式显示当前的所有的进程               ps -ef |grep redis   查看进程 过滤为redis的进程
    - -e 显示所有进程
    - -f 全格式

#### 结束进程 kill killall

- kill [选项] 进程号
    - -9 强制终止
- killall 进程名称，支持通配符  killall a*

#### 查看进程树 pstree

- pstree [选项]
    - -p 显示pid
    - -u 显示用户

### 动态监控程序 top

- top [选项]  能看 任务量、cpu、内存、虚拟内存，使用情况
    - -d 指定系统状态更新的时间(默认3s) top -d 10
    - -i 使top不显示任何闲置或僵尸进程
    - -p 通过指定监控进程ID来仅仅监控某个进程的状态
    - 案例：监视特定用户：输入top 然后输入 “u” 然后输入用户名(root)
    - 案例：终止指定进程：top 输入k 输入pid

- 互动指令：top运行过程中
    - P：按照CPU使用率从大到小排，默认就是此项
    - M：按照内存使用率从大到小排
    - N：以PID排序从大到小
    - q：退出top监控程序

### 监控网络状态 netstat

- netstat [选项] netstat -anp
    - -an 按一定顺序排列输出
    - -p 显示哪个进程在调用
    - 查看服务名为sshd的服务的信息：netstat -anp | grep sshd

### ssh

> ssh 链接远程服务器出现错误：eg: ssh root@ip地址
>
> @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
> @    WARNING: REMOTE HOST IDENTIFICATION HAS CHANGED!     @
> @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
> IT IS POSSIBLE THAT SOMEONE IS DOING SOMETHING NASTY!
> Someone could be eavesdropping on you right now (man-in-the-middle attack)!
> It is also possible that a host key has just been changed.
> The fingerprint for the ECDSA key sent by the remote host is
> SHA256:Qy6/8OuWO9nqfgls5vmSST//fyaOzROHqeP8S+KJx1w.
> Please contact your system administrator.
> Add correct host key in C:\\Users\\13949/.ssh/known_hosts to get rid of this message.
> Offending ECDSA key in C:\\Users\\13949/.ssh/known_hosts:1
> ECDSA host key for 122.9.46.43 has changed and you have requested strict checking.
> Host key verification failed.

- 原因：第一次ssh链接的时候会生成一个认证凭据，存储在客户端中的known_hosts，如果服务器地址重置or重新安装了，就会产生这个问题。
- 解决：ssh-keygen -R 服务器ip地址 然后重新链接 恰好我的服务器中病毒之后老师重新安装了，所以正好是这个问题。

# Shell编程

### Shell脚本编写

#### 脚本格式要求

1. **脚本以#!/bin/bash 开头**(现在不需要也可以)
2. **脚本需要有可执行权限**
3. chmod 744 赋予权限         rwx：421  可以用相对路径和绝对路径来执行
4. 不给可执行权限也可以运行，但是不推荐。sh ./hello.sh

#### Shell变量

##### 变量介绍

1. Linux变量分为**系统变量**和**用户自定义变量**
2. 系统变量：$HOME $PWD $SHELL $USER 等等
3. 显示当前shell中所有变量：set

##### Shell变量的定义

1. 定义变量：变量=值
2. 撤销变量：unset 变量
3. 声明静态变量：readonly 变量 不能unset

##### 定义变量规则

1. 变量名称可以有字母、数字、下划线组成，不能以数字开头。
2. 等号两侧不能有空格
3. 变量名称一般习惯为大写

##### 将命令的返回值赋值给变量

1. A=`ls -l` /home 反引号
2. A=$(ls -l /home) $()

#### 设置环境变量

##### 基本语法

1. export 变量名=值 (在配置文件中，将shell变量输出为环境变量)
2. source 配置文件 source /etc/profile (让修改后的配置立即生效)
3. echo $变量名

#### 位置参数变量

##### 介绍

当我们执行Shell脚本并携带参数时，如果想要得到命令行的信息，可以使用位置参数变量。 ./hello.sh 100 200

##### 基本语法

1. $n (n为数字，$0代表命令本身，$1-$9 代表第一到第九个参数，10以上需要大括号 ${11})
2. $* (这个变量代表命令行中所有的参数 $* 把参数看成一个整体)
3. $@ (这个变量代表命令行中所有的参数 $@ 把每个参数区分对待)
4. $# (这个变量代表参数的个数)

#### 预定义变量

1. $$ (当前进程的进程号)
2. $! (后台运行的最后一个进程的进程号)
3. $? (最后一次执行命令的返回状态。为0代表上一个命令正确执行，非0则不正确执行)

#### 运算符

##### 基本语法

1. $((运算式)) 或者 $[运算式]
2. expr m + n 注意运算符间要有空格   ‘\’* / % 乘(乘号前面需要转移符号)除取余

#### 条件判断

##### 基本语法

1. [ condition ] (condition 前后要有空格)
2. 非空返回true

##### 判断语句

- 两个整数的比较

    - = 字符串比较

    - -lt 小 于

    - -le 小于等于

    - -eq 等 于

    - -gt 大 于

    - -ge 大于等于
    - -ne 不等于

- 按照文件权限进行判断

    - -r 有读的权限  [ -r  文件  ]
    - -w 有写的权限
    - -x 有执行的权限

- 按照文件类型进行判断

    - -f 文件存在并且是一个常规的文件
    - -e 文件存在
    - -d 文件存在并是一个目录
#### 流程控制

##### if、case判断

> if [ 条件判断式 ]
>
> ​	then
>
> ​		程序
>
> ​		elif [条件判断式]
>
> ​			then
>
> ​					程序
>
> fi

> case $变量名 in
>
> "值 1"）
>
> 如果变量的值等于值 1，则执行程序 1
>
> ;;
>
> "值 2"）
>
> 如果变量的值等于值 2，则执行程序 2
>
> ;;
>
> …省略其他分支…
>
> *）
>
> 如果变量的值都不是以上的值，则执行此程序
>
> ;;
>
> esac

##### for循环

> for 变 量 in 值 1 值 2 值 3…
> do
>	程序
> done

> for ((i=1;i<=100;i++))
>
> do
>
> ​	程序
>
> done

##### while循环

> while [ 条件判断式 ]
>
> do
>
> 程序
>
> done

#### read读取控制台输入

##### 基本语法

- read [选项] [参数]
    - -p 读取时的提示符
    - -t 读取值时的等待时间

#### 函数

##### 系统函数

- basename [pathname] [suffix] (返回完整路径最后 / 的部分，常用于获取文件名)
    - pathname 路径
    - suffix 为后缀，如果 suffix 被指定了，basename 会将 pathname 中的 suffix 去掉。
- dirname (返回完整路径最后 / 的前面的部分，常用于返回路径部分)

##### 自定义函数

- 基本语法  funtion funtionName(){
- return;
- }

