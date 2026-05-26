<template>
  <view class="mine-page">
    <view class="profile-card">
      <image src="/static/avatar.png" mode="aspectFill" class="avatar" />
      <view class="profile-info">
        <view class="name">配送员</view>
        <view class="desc">今日配送 · 稳稳送达</view>
      </view>
      <view class="tag">ON DUTY</view>
    </view>

    <view class="quick-card" @click="goDelivering">
      <view>
        <view class="quick-title">配送中的订单</view>
        <view class="quick-desc">首页可查看当前待处理订单</view>
      </view>
      <text class="arrow">›</text>
    </view>

    <view class="menu-section">
      <view class="menu-item logout" @click="doLogout">
        <text>退出登录</text>
      </view>
    </view>
  </view>
</template>

<script setup>
function goDelivering() {
  uni.switchTab({ url: '/pages/index/index' })
}

function doLogout() {
  uni.removeStorageSync('token')
  uni.reLaunch({ url: '/pages/login/login' })
}
</script>

<style scoped>
.mine-page { min-height: 100vh; padding: 16px 14px 28px; background: #f6f1e8; box-sizing: border-box; }
.profile-card { position: relative; display: flex; align-items: center; overflow: hidden; min-height: 126px; padding: 20px 16px; color: #fff2d8; border-radius: 30px; background: radial-gradient(circle at 88% 12%, rgba(226,192,120,.38), transparent 34%), linear-gradient(135deg, #214f40, #17372e); box-shadow: 0 18px 34px rgba(31,90,70,.22); }
.avatar { width: 68px; height: 68px; border-radius: 34px; border: 2px solid rgba(255,242,216,.76); background: #efe2ce; }
.profile-info { flex: 1; min-width: 0; margin-left: 14px; }
.name { font-size: 21px; font-weight: 900; }
.desc { font-size: 12px; color: rgba(255,242,216,.76); margin-top: 6px; }
.tag { position: absolute; right: 14px; top: 14px; padding: 4px 8px; color: #1f5a46; background: #e2c078; border-radius: 999px; font-size: 10px; letter-spacing: 1px; font-weight: 900; }
.quick-card, .menu-section { margin-top: 14px; padding: 16px; border-radius: 24px; background: #fffaf2; border: 1px solid #eadbc4; box-shadow: 0 12px 24px rgba(83,59,33,.08); }
.quick-card { display: flex; align-items: center; justify-content: space-between; }
.quick-title { color: #22352d; font-size: 16px; font-weight: 900; }
.quick-desc { color: #9a8d78; font-size: 12px; margin-top: 5px; }
.arrow { color: #b8aa93; font-size: 24px; }
.menu-item { display: flex; justify-content: center; padding: 8px 0; color: #8a5a2b; font-size: 15px; font-weight: 800; }
</style>
