#用来测试webhook是否生效</br>
配置过程</br>
1、配置jekins[需要特别注意安全组的配置，不然会出各种小错误]</br>
POST /job/dynamic-log/build    403 Forbidden </br>
POST /job/dynamic-log/build    403 No valid crumb was included in the request </br>
2、配置为工程的代码库绑定hook</br>
3、更新代码触发hook</br>
4、去Jenkins查看任务是否执行
注意：Jenkins要去构建任务栏看，不然容易看不到输出
