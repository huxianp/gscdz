#一、
###第一次运行前时候自己建个数据库 charset 记得改 utf8，propertis 文件里的 url、password 记得改成自己的。
#二、
###做完以后看看 config 下的 WebSecurity 这个文件，getPersistentTokenRepository 这个方法里一行注释掉的，把注释去掉，运行的时候会生成一张 persistent_logins 的表（第二次及以后运行的时候这行代码就可以注释掉了，不然这张表里一直没东西）
#三、
###做完之后就跑一下吧。表建完以后，用 workbench 把那个 sql 文件打开，执行一下把数据导进去。执行前看看 sql 文件开头的开头的 use……..; 那句话 改成自己的数据库的名字嗷。做完以上以后就 okay 了。重启一下项目吧，记得帮上面说的那行代码注释掉。

##用户 id 有 zhangsan、lisi、wangwu，管理员 sunliu，密码都是 123456.有需要自己加。注册能加、管理员端能批量导入