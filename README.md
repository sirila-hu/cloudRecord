# 云笔记系统

### 版本日志

* v0.2.1:
  1. UI部分更新：弹窗esc关闭和回车确认功能（放弃使用layer.prompt，而改用layer.open自定义）,添加加载动画；
  2. 功能部分：实现笔记本自定义排序，添加令牌刷新，实现用户管理模块（暂只有修改密码功能）；
  3. 视频上传查看功能和图片令牌问题顺延至下个版本。

+ v0.2.0 ：
  1. 完成了笔记创建、书写、保存等主要功能的实现，以及图片上传功能(图片获取还有缺陷)；
  2. 采用JWT来进行认证授权管理；
  3. 使用vue重构了前端部分；
  4. <u>下个版本期望：前端需要添加加载动画以防止报错，笔记本添加自定义顺序，修改弹窗加上回车确定，图片获取需要加上认证，视频上传功能，刷新令牌功能，用户管理功能。</u>

### 技术组成

* 前端：vue（主体框架）、bootstrap4（UI组成）、jquery（ajax请求）、vditor（笔记编辑）、layer（弹窗提示）
* 后端：java（主要语言）、springboot(使用框架)
* 数据库：mysql

### 笔记本组成

从上到下主要分为，笔记本（record）、分区（section）、页（page）、页文件（pageFile）,其为包含关系，前三个为数据库中存储，最后的页文件为文件系统存储。

### 功能模块

> 主要模块： recordMan、recordEdit、pageEdit、imgMan、search、userMan、authentication

* recordMan（笔记本管理模块）：主要用于管理笔记本层次相关的信息，对于笔记本的删除将会导致其下属所有内容被删除，其对应前端的recordMan视图；
* recordEdit（笔记本编辑模块）：主要涉及笔记本关联的分区和页的管理，对分区和页的删除同样会导致下属内容被删除，其对应前端的recordApplet视图的recordEdit子组件；
* pageEdit（页编辑模块）：主要涉及对页对应的页文件的保存(新建和修改)和删除工作，其对应前端的recordApplet视图的pageEdit子组件；
* imgMan（图片管理模块）：包含图片的上传和删除，为了方便管理和防止对服务其造成积压，将图片管理模块从页编辑中独立出来(并将上传的图片进行复用)，图片上传后将存放在服务器对应的用户文件夹里，使用时直接使用markdown的调用语句来调用图片，不做图片和页的直接关联，在对应管理界面可以删除上传的图片，其对应前端imgMan视图；
* search（搜索模块）：主要涉及对用户关联的笔记及其内容进行关键字搜索，然后在前端显示搜索结果，其搜索逻辑为先搜索数据库中匹配关键字的项，在去搜索数据库中不匹配项的页文件看是否含有关键字，在读取页文件时为了缩短搜索时间，采用逐行读取，只要一匹配到关键字立马结束改页文件的搜索，其对应前端的recordResult视图；
* userMan（用户管理模块）：用于管理用户相关的信息，暂时只有修改密码功能；
* authentication（登陆认证管理模块）：主要涉及到系统的登陆认证，采用JWT的认证方式。

