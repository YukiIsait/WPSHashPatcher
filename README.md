# WPS Office 配置文件哈希校验修补程序

本修补程序用于使 WPS Office 的安装程序和主程序跳过对配置文件的哈希校验。

## 使用方式

```txt
java -jar wps-hash-patcher.jar <executableFilePath> [options]
```

### 参数

- `executableFilePath`
    - 目标可执行文件
    - 类型：String

### 选项

- `--architecture, -a`
    - 目标可执行文件架构
    - 类型：i386 / amd64
    - 默认值：i386

## 开源许可

本项目根据 MIT 许可证授权，详见 [LICENSE](LICENSE.md) 文件。
