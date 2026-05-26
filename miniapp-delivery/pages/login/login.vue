<template>
  <view class="login-page">
    <view class="brand-card">
      <view class="brand-kicker">DELIVERY</view>
      <view class="brand-title">甜品配送端</view>
      <view class="brand-subtitle">把今日新鲜送到客人手里</view>
    </view>

    <view class="login-card">
      <view class="form-title">配送员登录</view>
      <input class="input" v-model="username" placeholder="请输入账号" placeholder-class="placeholder" />
      <input class="input" v-model="password" placeholder="请输入密码" placeholder-class="placeholder" password />
      <button class="btn" :loading="loading" @click="doLogin">登 录</button>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { login } from '@/api/index'

const username = ref('')
const password = ref('')
const loading = ref(false)

async function doLogin() {
  if (!username.value || !password.value) {
    uni.showToast({ title: '请输入账号密码', icon: 'none' })
    return
  }
  loading.value = true
  try {
    const data = await login(username.value, password.value)
    uni.setStorageSync('token', data.token)
    uni.reLaunch({ url: '/pages/index/index' })
  } catch { /* */ }
  finally { loading.value = false }
}
</script>

<style scoped>
.login-page { min-height: 100vh; padding: 44px 20px; background: radial-gradient(circle at 84% 8%, rgba(226,192,120,.32), transparent 30%), linear-gradient(180deg, #17372e 0%, #214f40 42%, #f6f1e8 42%); box-sizing: border-box; }
.brand-card { color: #fff2d8; padding: 22px 4px 34px; }
.brand-kicker { display: inline-block; padding: 4px 10px; border: 1px solid rgba(226,192,120,.55); border-radius: 999px; color: #e2c078; font-size: 10px; letter-spacing: 3px; }
.brand-title { margin-top: 18px; font-size: 30px; font-weight: 900; }
.brand-subtitle { margin-top: 8px; color: rgba(255,242,216,.72); font-size: 13px; }
.login-card { margin-top: 8px; padding: 24px 18px 22px; border-radius: 28px; background: #fffaf2; border: 1px solid #eadbc4; box-shadow: 0 20px 38px rgba(56,40,23,.16); }
.form-title { color: #22352d; font-size: 20px; font-weight: 900; margin-bottom: 18px; }
.input { height: 48px; padding: 0 16px; margin-bottom: 14px; border-radius: 18px; background: #f5ead8; color: #22352d; font-size: 14px; border: 1px solid #eadbc4; }
.placeholder { color: #9a8d78; }
.btn { margin-top: 8px; height: 48px; line-height: 48px; border-radius: 24px; color: #fff2d8; background: linear-gradient(135deg, #23634c, #153c31); font-size: 16px; box-shadow: 0 12px 22px rgba(31,90,70,.24); }
</style>
