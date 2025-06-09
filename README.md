# 超市管理系统

一个基于 Java Web 技术栈开发的小型超市管理系统，提供商品管理、销售管理、库存管理和财务管理等核心功能。

## 🚀 系统特性

### 核心功能模块
- **用户登录系统** - 多角色权限管理（管理员、销售员、库管员、财务员）
- **商品管理** - 商品增删改查、分类管理、库存显示
- **销售管理** - 销售记录、详情查看、统计报表、动态计算
- **库存管理** - 入库/出库/调整、变动记录、低库存预警
- **财务管理** - 资金占用记录、统计分析、图表展示

### 技术特点
- **前端技术**: JSP + CSS + JavaScript + Chart.js
- **后端技术**: Java Servlet + JDBC
- **数据库**: MySQL 5.7+（支持完整的关系型数据库功能）
- **架构模式**: MVC 三层架构（Model-View-Controller）
- **设计模式**: DAO 模式、单例模式

## 📋 系统要求

### 开发环境
- **JDK**: 1.8 或更高版本
- **Web容器**: Tomcat 9.0 或更高版本
- **IDE**: IntelliJ IDEA / Eclipse（可选）
- **数据库**: MySQL 5.7 或更高版本
- **数据库驱动**: MySQL Connector/J 8.0+（已包含）

### 运行环境
- **操作系统**: Windows / macOS / Linux
- **内存**: 最少 1GB RAM
- **磁盘空间**: 最少 500MB
- **MySQL服务**: 需要运行MySQL数据库服务

## 🛠️ 安装部署

### 配置开始导入

#### 1. MySQL 数据库准备

**安装 MySQL**
```bash
# macOS (使用 Homebrew)
brew install mysql
brew services start mysql

# Ubuntu/Debian
sudo apt update
sudo apt install mysql-server
sudo systemctl start mysql

# CentOS/RHEL
sudo yum install mysql-server
sudo systemctl start mysqld
```

**创建数据库和用户**
```sql
-- 登录 MySQL
mysql -u root -p

-- 创建数据库
CREATE DATABASE supermarket_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建用户（可选，也可以使用root用户）
CREATE USER 'supermarket'@'localhost' IDENTIFIED BY '123456';
GRANT ALL PRIVILEGES ON supermarket_db.* TO 'supermarket'@'localhost';
FLUSH PRIVILEGES;
```

**导入数据库结构和初始数据**
```bash
# 进入项目目录
cd /Users/livio/Desktop/DBResources/Final09

# 导入数据库脚本
mysql -u root -p < src/main/resources/database/schema.sql
```

#### 2. 配置数据库连接

数据库连接配置在 `DBUtil.java` 中，当前配置为：
```java
private static final String URL = "jdbc:mysql://localhost:3306/supermarket_db?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai";
private static final String USERNAME = "root";
private static final String PASSWORD = "123456";
```

**如需修改数据库连接信息**，请编辑 <mcfile name="DBUtil.java" path="/Users/livio/Desktop/DBResources/Final09/src/main/java/com/supermarket/util/DBUtil.java"></mcfile>：
- 修改 `URL` 中的主机地址、端口、数据库名
- 修改 `USERNAME` 和 `PASSWORD` 为您的MySQL用户凭据

### 方法一：使用 IDE 部署（推荐开发环境）

#### 1. 导入项目
```bash
# 克隆或下载项目到本地
git clone <repository-url>
# 或直接解压项目文件夹
```

#### 2. 配置 IntelliJ IDEA
1. 打开 IntelliJ IDEA
2. 选择 "Open" 打开项目文件夹
3. 等待项目索引完成
4. 配置 Project SDK（JDK 1.8+）
   - File -> Project Structure -> Project -> Project SDK
   - 选择已安装的 JDK 版本
5. 添加 MySQL 驱动依赖
   - 确保 `lib/` 目录下有 `mysql-connector-java-8.0.x.jar`

#### 3. 配置 Tomcat
1. 点击右上角 "Add Configuration" -> "+" -> "Tomcat Server" -> "Local"
2. 配置 Tomcat 安装路径
   - Application server: 选择 Tomcat 安装目录
3. 在 "Deployment" 选项卡中添加 Artifact
   - 点击 "+" -> "Artifact" -> 选择 "Final09:war exploded"
4. 设置 Application context 为 "/supermarket"
5. 配置端口（默认 8080）

#### 4. 运行项目
1. 确保 MySQL 服务正在运行
2. 点击 "Run" 按钮启动 Tomcat
3. 浏览器访问：`http://localhost:8080/supermarket`
4. 使用测试账号登录（见下方默认账号）

### 方法二：手动部署到 Tomcat

#### 1. 准备依赖库
```bash
# 确保项目 lib 目录包含以下 JAR 文件：
# - mysql-connector-java-8.0.x.jar
# - servlet-api.jar
# - jsp-api.jar
```

#### 2. 编译项目
```bash
# 进入项目目录
cd /Users/livio/Desktop/DBResources/Final09

# 创建编译目录
mkdir -p build/classes

# 编译 Java 源文件
javac -cp "lib/*:$TOMCAT_HOME/lib/*" -d build/classes src/main/java/com/supermarket/**/*.java
```

#### 3. 打包 WAR 文件
```bash
# 创建 WAR 包目录结构
mkdir -p build/war/WEB-INF/classes
mkdir -p build/war/WEB-INF/lib

# 复制编译后的类文件
cp -r build/classes/* build/war/WEB-INF/classes/

# 复制依赖库
cp lib/* build/war/WEB-INF/lib/

# 复制 web 资源
cp -r src/main/webapp/* build/war/

# 创建 WAR 文件
cd build/war
jar -cvf ../supermarket.war *
```

#### 4. 部署到 Tomcat
```bash
# 复制 WAR 文件到 Tomcat webapps 目录
cp build/supermarket.war $TOMCAT_HOME/webapps/

# 启动 Tomcat
$TOMCAT_HOME/bin/startup.sh  # Linux/macOS
# 或
$TOMCAT_HOME/bin/startup.bat  # Windows
```

### 方法三：Docker 部署（推荐生产环境）

#### 1. 创建 docker-compose.yml
```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: 123456
      MYSQL_DATABASE: supermarket_db
      MYSQL_CHARACTER_SET_SERVER: utf8mb4
      MYSQL_COLLATION_SERVER: utf8mb4_unicode_ci
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
      - ./src/main/resources/database/schema.sql:/docker-entrypoint-initdb.d/schema.sql
    
  app:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - mysql
    environment:
      DB_HOST: mysql
      DB_PORT: 3306
      DB_NAME: supermarket_db
      DB_USER: root
      DB_PASSWORD: 123456

volumes:
  mysql_data:
```

#### 2. 创建 Dockerfile
```dockerfile
FROM tomcat:9.0-jdk8

# 删除默认应用
RUN rm -rf /usr/local/tomcat/webapps/*

# 复制应用
COPY build/supermarket.war /usr/local/tomcat/webapps/ROOT.war

# 暴露端口
EXPOSE 8080

# 启动命令
CMD ["catalina.sh", "run"]
```

#### 3. 构建和运行
```bash
# 构建镜像
docker-compose build

# 运行容器
docker-compose up -d
```

## 🗄️ 数据库配置

### MySQL 数据库特性
项目使用 MySQL 作为数据库，具有以下优势：
- 成熟稳定的关系型数据库
- 支持完整的 ACID 事务
- 优秀的性能和并发处理能力
- 丰富的数据类型和函数
- 完善的备份和恢复机制

### 数据库连接配置
数据库连接配置在 `DBUtil.java` 中：
```java
private static final String URL = "jdbc:mysql://localhost:3306/supermarket_db?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai";
private static final String USERNAME = "root";
private static final String PASSWORD = "123456";
```

### 数据库结构
系统包含以下数据表：
- `products` - 商品信息表
- `sales` - 销售记录表
- `sale_details` - 销售明细表
- `inventory_changes` - 库存变动表
- `fund_occupation` - 资金占用表
- `users` - 用户表

## 👥 默认用户账号

系统预置了以下测试账号：

| 用户名 | 密码 | 角色 | 权限说明 |
|--------|------|------|----------|
| admin | admin123 | 管理员 | 所有功能权限 |
| cashier1 | cash123 | 销售员 | 销售管理权限 |
| warehouse1 | ware123 | 库管员 | 库存管理权限 |
| finance1 | fin123 | 财务员 | 财务管理权限 |

## 🌐 系统访问

### 访问地址
- **本地开发**: `http://localhost:8080/supermarket`
- **生产环境**: `http://your-server-ip:8080/supermarket`

### 功能模块
1. **用户登录** - `/` 或 `/index.jsp`
2. **主页面** - `/main.jsp`
3. **商品管理** - `/jsp/product/`
4. **销售管理** - `/jsp/sale/`
5. **库存管理** - `/jsp/inventory/`
6. **财务管理** - `/jsp/fund/`

## 📁 项目结构

```
Final09/
├── README.md                    # 项目说明文档
├── lib/                         # 依赖库
│   ├── mysql-connector-java-8.0.x.jar
│   ├── servlet-api.jar
│   └── jsp-api.jar
└── src/
    └── main/
        ├── java/
        │   └── com/supermarket/
        │       ├── dao/             # 数据访问层
        │       ├── model/           # 数据模型
        │       ├── service/         # 业务逻辑层
        │       ├── servlet/         # 控制层
        │       ├── util/            # 工具类
        │       │   └── DBUtil.java  # 数据库连接工具
        │       └── filter/          # 过滤器
        ├── resources/
        │   └── database/
        │       └── schema.sql       # MySQL数据库脚本
        └── webapp/
            ├── WEB-INF/
            │   └── web.xml          # Web应用配置
            ├── css/                 # 样式文件
            ├── js/                  # JavaScript文件
            ├── jsp/                 # JSP页面
            └── index.jsp            # 登录页面
```

## 🔧 故障排除

### 常见问题

1. **数据库连接失败**
   ```
   错误: Communications link failure
   解决: 检查MySQL服务是否启动，确认连接参数正确
   ```

2. **字符编码问题**
   ```
   错误: 中文显示乱码
   解决: 确保数据库、连接URL、页面编码都设置为UTF-8
   ```

3. **权限不足**
   ```
   错误: Access denied for user
   解决: 检查MySQL用户权限，确保有数据库访问权限
   ```

4. **端口冲突**
   ```
   错误: Port 8080 already in use
   解决: 修改Tomcat端口或停止占用8080端口的进程
   ```

### 日志查看
- **Tomcat日志**: `$TOMCAT_HOME/logs/catalina.out`
- **MySQL日志**: `/var/log/mysql/error.log`
- **应用日志**: 控制台输出

## ⚡ 性能优化

### 数据库优化
1. **索引优化**
   ```sql
   -- 为常用查询字段添加索引
   CREATE INDEX idx_product_name ON products(product_name);
   CREATE INDEX idx_sale_date ON sales(sale_date);
   CREATE INDEX idx_receipt_product ON sale_details(receipt_id, product_id);
   ```

2. **连接池配置**
   - 建议使用 HikariCP 或 C3P0 连接池
   - 配置合适的最大连接数和超时时间

3. **查询优化**
   - 避免使用 SELECT *
   - 合理使用分页查询
   - 优化复杂的 JOIN 查询

### 应用优化
1. **缓存策略**
   - 对商品信息等相对静态的数据进行缓存
   - 使用 Redis 或 Memcached

2. **静态资源**
   - 启用 Gzip 压缩
   - 使用 CDN 加速
   - 合并和压缩 CSS/JS 文件

## 🚀 开发指南

### 代码规范
1. **命名规范**
   - 类名使用 PascalCase
   - 方法名和变量名使用 camelCase
   - 常量使用 UPPER_SNAKE_CASE

2. **注释规范**
   - 类和方法必须有 JavaDoc 注释
   - 复杂逻辑需要行内注释
   - 数据库字段需要 COMMENT

### 扩展开发
1. **添加新功能模块**
   - 创建对应的 Model、DAO、Service、Servlet
   - 添加相应的 JSP 页面
   - 更新导航菜单

2. **数据库变更**
   - 更新 `schema.sql`
   - 创建数据库迁移脚本
   - 更新相关的 Model 类

## 🧪 测试

### 功能测试
1. **用户登录测试**
   - 测试各角色登录
   - 测试权限控制

2. **业务功能测试**
   - 商品管理 CRUD 操作
   - 销售流程完整性
   - 库存变动准确性
   - 财务统计正确性

### 性能测试
```bash
# 使用 Apache Bench 进行压力测试
ab -n 1000 -c 10 http://localhost:8080/supermarket/
```

## 📄 许可证

本项目采用 MIT 许可证，详情请参阅 LICENSE 文件。

## 🤝 贡献

欢迎提交 Issue 和 Pull Request 来改进项目。

## 📞 支持

如有问题，请通过以下方式联系：
- 提交 GitHub Issue
- 发送邮件至项目维护者

---

**注意**: 请确保在生产环境中修改默认密码和数据库配置，以保证系统安全。
```
