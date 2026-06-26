# 图书馆管理系统

基于 Spring Boot 3 + Thymeleaf + MySQL 的图书借阅管理系统，支持双角色权限、多维检索、读者荐购等功能。

---

## 环境要求

- Java 17+
- MySQL 8.0+
- Maven 3.6+

---

## 快速启动

### 1. 创建数据库

```sql
CREATE DATABASE library_db DEFAULT CHARACTER SET utf8mb4;
```

### 2. 修改数据库配置

打开 `src/main/resources/application.properties`，按实际情况修改：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/library_db?useSSL=false&serverTimezone=Asia/Shanghai
spring.datasource.username=你的数据库用户名
spring.datasource.password=你的数据库密码
```

### 3. 启动项目

**IDEA：** 找到 `LibrarySystemApplication.java`，右键 → Run

### 4. 访问系统

| 页面 | 地址 |
|------|------|
| 首页 | http://localhost:8080 |
| 登录 | http://localhost:8080/user/login |
| 注册 | http://localhost:8080/user/register |

---

## 默认管理员账号

首次启动自动创建：

| 用户名 | 密码 | 角色 |
|--------|------|------|
| `admin` | `admin123` | 管理员（ROLE_ADMIN） |

---

## 角色与权限

### 普通用户（注册后默认）

| 功能 | 页面/路由 |
|------|-----------|
| 图书检索（多维度搜索） | `/books` |
| 图书详情查看 | `/books/detail/{id}` |
| 读者荐购（提交购书建议） | `/books/recommend` |
| 荐购记录查看 | `/books/my-recommendations` |
| 个人信息编辑 | `/user/center` |
| 修改密码 | `/user/center` → 安全设置 |
| 借阅历史查看 | `/user/center` → 借阅历史 |
| 删除自己的账户 | `/user/center` → 安全设置 |

### 管理员

> 登录后自动跳转管理员后台 `/admin/books`

| 功能 | 页面/路由 |
|------|-----------|
| 图书管理（CRUD 含检索） | `/admin/books` |
| 添加图书（含分类/出版社等字段） | `/admin/books/add` |
| 编辑图书 | `/admin/books/edit/{id}` |
| 用户管理（列表/编辑/切换角色/删除） | `/admin/users` |
| 编辑用户信息/权限 | `/admin/users/edit/{id}` |
| 荐购管理（采纳/拒绝/回复） | `/admin/recommendations` |

---

## 项目结构

```
LibrarySystem/
├── src/main/java/com/example/librarysystem/
│   ├── config/
│   │   ├── SecurityConfig.java          # 安全配置、角色权限、CSRF
│   │   ├── DataInitializer.java         # 启动时初始化管理员账号
│   │   └── GlobalExceptionHandler.java  # 全局异常处理
│   ├── controller/
│   │   ├── MainController.java          # 首页、登录、注册
│   │   ├── BookController.java          # 普通用户：图书检索/详情/荐购
│   │   ├── AdminController.java         # 管理员：图书CRUD/用户/荐购管理
│   │   ├── UserController.java          # REST API：个人信息/密码/借阅
│   │   └── UserCenterViewController.java # 用户中心页面
│   ├── dto/                             # 数据传输对象（含校验注解）
│   ├── entity/
│   │   ├── User.java                    # 用户（实现 UserDetails）
│   │   ├── Book.java                    # 图书（含分类/出版社/索书号等）
│   │   ├── BorrowRecord.java            # 借阅记录
│   │   └── PurchaseRecommendation.java  # 读者荐购
│   ├── exception/                       # 自定义异常
│   ├── repository/                      # JPA 数据访问层
│   └── service/                         # 业务逻辑层
├── src/main/resources/
│   ├── templates/
│   │   ├── fragments/layout.html        # 侧边栏布局
│   │   ├── admin/                       # 管理员页面（5个）
│   │   └── ...                          # 其他页面（12个）
│   └── application.properties           # 应用配置
└── pom.xml
```

---

## 主要功能

### 图书检索

- **多维度搜索**：按题名、作者、ISBN、分类号检索
- **分类浏览**：侧边栏显示所有分类，一键筛选
- **新书通报**：最新入库图书列表
- **热门排行**：按借阅次数排序
- **图书详情**：完整书目信息（分类、出版社、页数、语种、索书号）

### 读者荐购

- **提交建议**：普通用户填写书名、作者、出版社、ISBN、推荐理由
- **状态追踪**：查看自己的荐购记录（待处理/已采纳/已拒绝）
- **审核处理**：管理员可采纳或拒绝，并填写回复

### 用户管理

- **个人信息**：编辑姓名、邮箱、用户名
- **安全设置**：修改密码、删除账户
- **后台管理**：管理员可查看所有用户、编辑信息、切换角色（普通用户↔管理员）、删除用户

### 统一界面

- **侧边栏导航**：根据登录角色显示不同菜单，当前页面自动高亮
- **响应式布局**：移动端自动折叠侧边栏
- **统一配色**：深蓝色主色调 + 橙色强调色

---

## 注意事项

- 数据库表结构由 JPA 自动创建（`ddl-auto=update`），无需手动建表
- 默认端口 8080，可在 `application.properties` 中修改
- 管理员账号 `admin / admin123` 首次启动时自动创建，请及时修改密码
- CSRF 保护已启用（API 路径和部分管理路径除外）
- `spring.jpa.open-in-view=false`，避免事务外懒加载异常
