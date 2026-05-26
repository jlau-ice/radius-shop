<template>
  <div class="login-page">
    <div class="login-orb orb-a"></div>
    <div class="login-orb orb-b"></div>

    <section class="brand-panel">
      <div class="brand-kicker">RADIUS DESSERT</div>
      <h1>把每一份甜品，做成有秩序的生意。</h1>
      <p>门店商品、订单、配送点位统一管理，适合本地测试环境快速 mock 登录。</p>
      <div class="brand-marks">
        <span>新中式茶饮</span>
        <span>甜品外送</span>
        <span>轻量后台</span>
      </div>
    </section>

    <el-card class="login-card">
      <div class="login-head">
        <div class="seal">甜</div>
        <div>
          <div class="login-title">甜品店管理后台</div>
          <div class="login-subtitle">本地测试默认开启 mock 登录</div>
        </div>
      </div>

      <el-form :model="form" :rules="rules" ref="formRef" label-width="0" class="login-form">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名" prefix-icon="User" size="large" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" show-password prefix-icon="Lock" size="large" @keyup.enter="doLogin" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="login-button" :loading="loading" @click="doLogin">进入管理台</el-button>
        </el-form-item>
      </el-form>

      <div class="mock-tip">
        <span>DEV MOCK</span>
        <strong>admin / 123456</strong>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '@/api/auth'

const router = useRouter()
const loading = ref(false)
const formRef = ref(null)
const form = reactive({ username: 'admin', password: '123456' })
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

async function doLogin() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    const data = await login(form.username, form.password)
    localStorage.setItem('token', data.token)
    ElMessage.success('登录成功')
    router.push('/')
  } catch { /* handled by interceptor */ }
  finally { loading.value = false }
}
</script>

<style scoped>
.login-page {
  position: relative;
  min-height: 100vh;
  display: grid;
  grid-template-columns: minmax(360px, 560px) 430px;
  align-items: center;
  justify-content: center;
  gap: 70px;
  padding: 48px;
  overflow: hidden;
  background:
    radial-gradient(circle at 18% 22%, rgba(195,154,85,.22), transparent 28%),
    radial-gradient(circle at 82% 80%, rgba(31,90,70,.18), transparent 30%),
    linear-gradient(135deg, #f8f0df 0%, #f6f1e8 52%, #edf3ec 100%);
}
.login-page::before {
  content: '';
  position: absolute;
  inset: 28px;
  border: 1px solid rgba(195,154,85,.28);
  border-radius: 36px;
  pointer-events: none;
}
.login-orb {
  position: absolute;
  border-radius: 999px;
  filter: blur(2px);
  opacity: .7;
}
.orb-a { width: 180px; height: 180px; left: -60px; bottom: 70px; background: rgba(31,90,70,.14); }
.orb-b { width: 120px; height: 120px; right: 18%; top: 12%; background: rgba(195,154,85,.2); }
.brand-panel, .login-card { position: relative; z-index: 1; }
.brand-kicker { color: #b78942; font-size: 12px; font-weight: 900; letter-spacing: 5px; }
.brand-panel h1 { max-width: 520px; margin: 18px 0 16px; color: #17372e; font-size: 54px; line-height: 1.08; font-weight: 900; letter-spacing: -2px; }
.brand-panel p { max-width: 430px; color: #796d5d; font-size: 16px; line-height: 1.9; }
.brand-marks { display: flex; gap: 10px; flex-wrap: wrap; margin-top: 34px; }
.brand-marks span { padding: 9px 14px; border-radius: 999px; color: #1f5a46; background: rgba(255,250,242,.72); border: 1px solid #eadbc4; font-size: 13px; font-weight: 800; }
.login-card { width: 430px; padding: 12px; border-radius: 30px !important; background: rgba(255,250,242,.92) !important; backdrop-filter: blur(18px); }
.login-head { display: flex; align-items: center; gap: 14px; margin-bottom: 30px; }
.seal { display: grid; place-items: center; width: 58px; height: 58px; color: #fff2d8; background: linear-gradient(135deg, #1f5a46, #17372e); border-radius: 20px; box-shadow: 0 14px 28px rgba(31,90,70,.26); font-size: 24px; font-weight: 900; }
.login-title { color: #17372e; font-size: 24px; font-weight: 900; }
.login-subtitle { margin-top: 5px; color: #9a8d78; font-size: 13px; }
.login-form { margin-top: 8px; }
.login-button { width: 100%; height: 46px; border-radius: 14px; font-size: 15px; font-weight: 900; letter-spacing: 2px; }
.mock-tip { display: flex; align-items: center; justify-content: space-between; margin-top: 12px; padding: 12px 14px; border-radius: 16px; background: #f6f1e8; border: 1px dashed #d8c4a8; color: #8d8271; }
.mock-tip span { color: #c39a55; font-size: 11px; font-weight: 900; letter-spacing: 2px; }
.mock-tip strong { color: #1f5a46; font-size: 13px; }
@media (max-width: 920px) {
  .login-page { grid-template-columns: 1fr; gap: 28px; padding: 28px; }
  .brand-panel h1 { font-size: 38px; }
  .login-card { width: 100%; max-width: 430px; }
}
</style>
