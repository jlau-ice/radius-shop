<script>
export default {
  onLaunch() {
    const token = uni.getStorageSync('token')
    if (!token) {
      uni.reLaunch({ url: '/pages/login/login' })
    }
  }
}
</script>

<style>
page {
  min-height: 100%;
  background: #f6f1e8;
  color: #22352d;
  font-family: "PingFang SC", "Hiragino Sans GB", serif;
}
button::after { border: 0; }
</style>
