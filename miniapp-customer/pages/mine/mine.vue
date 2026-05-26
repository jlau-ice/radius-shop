<template>
  <view class="mine">
    <view class="profile-card" @click="doLogin">
      <view class="profile-glow"></view>
      <image :src="user.avatarUrl || '/static/default-avatar.png'" mode="aspectFill" class="avatar" />
      <view class="user-text">
        <view class="nickname">{{ user.nickname || '点击登录' }}</view>
        <view class="phone">{{ user.phone || '登录后查看订单与配送进度' }}</view>
      </view>
      <view class="profile-tag">MEMBER</view>
    </view>

    <view class="quick-row">
      <view class="quick-item" @click="goOrders('pending')">
        <view class="quick-num">待</view>
        <text>待支付</text>
      </view>
      <view class="quick-item" @click="goOrders('delivering')">
        <view class="quick-num">送</view>
        <text>配送中</text>
      </view>
      <view class="quick-item" @click="goOrders('')">
        <view class="quick-num">单</view>
        <text>全部订单</text>
      </view>
    </view>

    <view class="menu-section">
      <view class="menu-title">服务</view>
      <view class="menu-item" @click="goOrders('')">
        <text>我的订单</text>
        <text class="arrow">›</text>
      </view>
      <view class="menu-item" @click="callService">
        <text>联系客服</text>
        <text class="arrow">›</text>
      </view>
    </view>

    <view class="menu-section" v-if="user.id">
      <view class="menu-item logout" @click="doLogout">
        <text>退出登录</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { wxLogin, getUserInfo, logout } from '@/api/index'

const user = ref({})

onMounted(async () => {
  const token = uni.getStorageSync('token')
  if (token) {
    try { user.value = await getUserInfo() } catch { /* */ }
  }
})

async function doLogin() {
  if (user.value.id) return
  uni.login({
    provider: 'weixin',
    success: async (res) => {
      try {
        const data = await wxLogin(res.code)
        uni.setStorageSync('token', data.token)
        user.value = data.user
      } catch { /* */ }
    }
  })
}

function goOrders(status) {
  uni.setStorageSync('orderStatusFilter', status)
  uni.switchTab({ url: '/pages/order/order' })
}

function callService() {
  uni.showToast({ title: '客服微信: xxxxx', icon: 'none', duration: 3000 })
}

async function doLogout() {
  try {
    await logout()
  } catch { /* */ }
  uni.removeStorageSync('token')
  user.value = {}
  uni.showToast({ title: '已退出', icon: 'none' })
}
</script>

<style scoped>
.mine { min-height: 100vh; padding: 16px 14px 28px; background: #f6f1e8; box-sizing: border-box; }
.profile-card { position: relative; display: flex; align-items: center; overflow: hidden; min-height: 126px; padding: 20px 16px; color: #fff2d8; border-radius: 30px; background: radial-gradient(circle at 88% 12%, rgba(226,192,120,.38), transparent 34%), linear-gradient(135deg, #214f40, #17372e); box-shadow: 0 18px 34px rgba(31,90,70,.22); box-sizing: border-box; }
.profile-glow { position: absolute; right: -44px; bottom: -58px; width: 150px; height: 150px; border: 1px solid rgba(226,192,120,.28); border-radius: 75px; }
.avatar { width: 68px; height: 68px; border-radius: 34px; border: 2px solid rgba(255,242,216,.76); background: #efe2ce; }
.user-text { flex: 1; min-width: 0; margin-left: 14px; }
.nickname { font-size: 21px; font-weight: 900; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.phone { font-size: 12px; color: rgba(255,242,216,.76); margin-top: 6px; }
.profile-tag { position: absolute; right: 14px; top: 14px; padding: 4px 8px; color: #1f5a46; background: #e2c078; border-radius: 999px; font-size: 10px; letter-spacing: 1px; font-weight: 900; }
.quick-row { display: flex; gap: 10px; margin-top: 14px; }
.quick-item { flex: 1; padding: 15px 0 12px; text-align: center; border-radius: 22px; background: #fffaf2; border: 1px solid #eadbc4; box-shadow: 0 10px 20px rgba(83,59,33,.07); }
.quick-num { width: 34px; height: 34px; line-height: 34px; margin: 0 auto 7px; border-radius: 17px; color: #fff2d8; background: #1f5a46; font-size: 14px; font-weight: 900; }
.quick-item text { color: #746856; font-size: 12px; }
.menu-section { margin-top: 14px; padding: 12px 14px; border-radius: 24px; background: #fffaf2; border: 1px solid #eadbc4; box-shadow: 0 12px 24px rgba(83,59,33,.08); }
.menu-title { color: #b78e4b; font-size: 11px; letter-spacing: 2px; font-weight: 900; margin-bottom: 4px; }
.menu-item { display: flex; justify-content: space-between; align-items: center; padding: 14px 0; border-bottom: 1px solid #f0e5d3; color: #22352d; font-size: 15px; font-weight: 700; }
.menu-item:last-child { border-bottom: 0; }
.arrow { color: #b8aa93; font-size: 22px; font-weight: 400; }
.logout { justify-content: center; color: #8a5a2b; }
</style>
