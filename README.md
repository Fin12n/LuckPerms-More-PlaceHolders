# LuckPerms-More-Placeholders

**LuckPerms-More-Placeholders** là một addon cho LuckPerms cung cấp các placeholder để hiển thị thời gian còn lại của temp groups theo thời gian thực.

## 🎯 Tính năng

- ✅ Placeholder hiển thị thời gian còn lại của temp group cụ thể
- ✅ Placeholder tự động phát hiện temp group hiện tại
- ✅ Hiển thị thời gian theo định dạng countdown (như 5d 3h 2m 15s)
- ✅ Tương thích từ Minecraft 1.8 đến 1.21.4
- ✅ Hỗ trợ PlaceholderAPI
- ✅ Cấu hình linh hoạt với config.yml và messages.yml
- ✅ Reload config không cần restart server

## 📋 Yêu cầu

- **Minecraft**: 1.8 - 1.21.4 (khuyến nghị 1.20.4)
- **Java**: 17 hoặc cao hơn
- **LuckPerms**: Phiên bản mới nhất
- **PlaceholderAPI**: Phiên bản mới nhất

## 🚀 Cài đặt

1. Tải file `.jar` của plugin
2. Đặt vào thư mục `plugins/` của server
3. Khởi động lại server
4. Plugin sẽ tự động tạo file `config.yml` và `messages.yml`

## 📊 Placeholders

### Placeholder chính

| Placeholder | Mô tả | Ví dụ |
|-------------|-------|-------|
| `%luckperms_cooldown_<group>%` | Thời gian còn lại của group cụ thể | `%luckperms_cooldown_vip%` |
| `%luckperms_cooldown_current%` | Thời gian còn lại của temp group hiện tại | Auto-detect temp group |

### Ví dụ sử dụng

```yaml
# Trong DeluxeMenus
display_name: '&aVIP Status: %luckperms_cooldown_vip%'

# Trong PlaceholderAPI
/papi parse <player> %luckperms_cooldown_current%

# Trong chat format
format: '&7[%luckperms_cooldown_current%] &f{player}: {message}'
```

## ⚙️ Lệnh

| Lệnh | Alias | Quyền | Mô tả |
|------|-------|-------|-------|
| `/lpplaceholder reload` | `/lppl reload` | `luckpermsplaceholders.admin` | Reload config |
| `/lpplaceholder check [player]` | `/lppl check [player]` | `luckpermsplaceholders.admin` | Kiểm tra temp group |
| `/lpplaceholder help` | `/lppl help` | `luckpermsplaceholders.admin` | Hiển thị trợ giúp |
| `/lpplaceholder info` | `/lppl info` | `luckpermsplaceholders.admin` | Thông tin plugin |

## 🔧 Cấu hình

### config.yml

```yaml
settings:
  # Định dạng thời gian hiển thị
  date-format: "dd/MM/yyyy HH:mm:ss"
  
  # Khoảng thời gian cập nhật (milliseconds)
  update-interval: 1000
  
  # Bật chế độ debug
  debug: false

placeholders:
  no-temp-group: "No temp group"
  expired: "Expired"
  permanent: "Permanent"
```

### messages.yml

File này chứa tất cả các thông điệp của plugin, hỗ trợ color codes với `&`.

## 📖 Hướng dẫn sử dụng

### 1. Kiểm tra temp group của player

```bash
/lppl check quang1807
```

### 2. Sử dụng placeholder trong DeluxeMenus

```yaml
items:
  vip_status:
    material: DIAMOND
    display_name: '&bVIP Status'
    lore:
      - '&7Time remaining: &f%luckperms_cooldown_vip%'
      - '&7Current temp group: &f%luckperms_cooldown_current%'
```

### 3. Sử dụng trong Scoreboard (như FeatherBoard)

```yaml
scoreboard:
  lines:
    - '&bVIP: &f%luckperms_cooldown_current%'
    - '&7Remaining: &f%luckperms_cooldown_vip%'
```

## 🛠️ Build từ source

### Yêu cầu

- Java 17+
- Maven 3.6+
- IntelliJ IDEA 2024.1 (khuyến nghị)

### Các bước build

```bash
# Clone repository
git clone https://github.com/quang1807/luckperms-more-placeholders.git
cd luckperms-more-placeholders

# Build với Maven
mvn clean package

# File .jar sẽ được tạo trong thư mục target/
```

## 🎨 Định dạng thời gian

Plugin hiển thị thời gian theo các định dạng sau:

- **< 1 phút**: `45s`
- **< 1 giờ**: `5m 30s`
- **< 1 ngày**: `2h 15m 30s`
- **≥ 1 ngày**: `3d 5h 15m 30s`

## 🔍 Troubleshooting

### Plugin không load được

1. Kiểm tra LuckPerms và PlaceholderAPI đã được cài đặt
2. Kiểm tra Java version ≥ 17
3. Xem log server để biết chi tiết lỗi

### Placeholder không hoạt động

1. Chạy `/papi reload` để reload PlaceholderAPI
2. Kiểm tra placeholder syntax có đúng không
3. Chạy `/lppl reload` để reload config

### Thời gian hiển thị sai

1. Kiểm tra timezone của server
2. Kiểm tra `date-format` trong config.yml
3. Kiểm tra LuckPerms temp group có đúng không

## 📝 License

MIT License - Xem file [LICENSE](LICENSE) để biết chi tiết.

## 👤 Tác giả

**quang1807**

- GitHub: [@quang1807](https://github.com/quang1807)

## 🤝 Đóng góp

Mọi đóng góp đều được hoan nghênh! Vui lòng:

1. Fork repository
2. Tạo feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Tạo Pull Request

## 📞 Hỗ trợ

Nếu bạn gặp vấn đề hoặc có câu hỏi:

1. Tạo [GitHub Issue](https://github.com/quang1807/luckperms-more-placeholders/issues)
2. Liên hệ qua Discord: `quang1807`

## 📈 Changelog

### v1.0.0
- ✨ Phát hành đầu tiên
- ✅ Hỗ trợ placeholder temp group cooldown
- ✅ Auto-detect temp group hiện tại
- ✅ Hệ thống lệnh hoàn chỉnh
- ✅ Cấu hình linh hoạt
