# WPS Office 配置文件哈希校验修补程序

> 本仓库已废弃，新的修补方式见 [WPSProfileVerificationPatch](https://github.com/YukiIsait/WPSProfileVerificationPatch) 仓库。

本修补程序用于使 WPS Office 的安装程序和主程序（krt.dll）跳过对配置文件的哈希校验。

## 使用方式

```txt
java -jar wps-hash-patcher.jar <filePath>
```

### 参数

- `filePath`
    - 目标可执行文件
    - 类型：String

## 开源许可

本项目根据 MIT 许可证授权，详见 [LICENSE](LICENSE.md) 文件。
