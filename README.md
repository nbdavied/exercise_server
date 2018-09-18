# 开发环境
IntellijIdea
# 运行配置
1. clone 项目代码
2. 找到 `/src/main/resources`  中的 `application.properties.sample` 文件，在本地拷贝一份副本，文件名为 `application.properties`
3. 把拷贝出的配置文件中的 `spring.datasource.url`，`spring.datasource.username`，`spring.datasource.password` 改为开发环境数据库连接
#接口
- `/api/question/next?bankid=BANKID&last=LAST` 按顺序获取下一题
- `/api/question/random?bankid=BANKID` 随机获取下一题
- `/api/question/answer/{id}` 获取答案
- `/api/question/{id}` 获取指定题目
- `/api/question/answer/{id}` 正确答案id数组
