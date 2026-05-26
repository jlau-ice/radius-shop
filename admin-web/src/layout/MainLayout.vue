<template>
  <el-container class="layout">
    <el-aside width="248px" class="aside">
      <div class="logo-block">
        <div class="logo-seal">甜</div>
        <div>
          <div class="logo-title">甜品店管理</div>
          <div class="logo-subtitle">Radius Dessert</div>
        </div>
      </div>

      <el-menu router :default-active="$route.path" class="nav-menu">
        <el-menu-item index="/dashboard">
          <el-icon><HomeFilled /></el-icon>
          <span>经营总览</span>
        </el-menu-item>
        <el-menu-item index="/category">
          <el-icon><Menu /></el-icon>
          <span>分类管理</span>
        </el-menu-item>
        <el-menu-item index="/product">
          <el-icon><Goods /></el-icon>
          <span>商品管理</span>
        </el-menu-item>
        <el-menu-item index="/order">
          <el-icon><Document /></el-icon>
          <span>订单管理</span>
        </el-menu-item>
        <el-menu-item index="/address">
          <el-icon><Location /></el-icon>
          <span>配送区域</span>
        </el-menu-item>
        <el-menu-item index="/delivery">
          <el-icon><User /></el-icon>
          <span>配送员管理</span>
        </el-menu-item>
      </el-menu>

      <div class="aside-footer">
        <span>APAC R2 READY</span>
        <strong>本地测试环境</strong>
      </div>
    </el-aside>

    <el-container class="main-wrap">
      <el-header class="header">
        <div>
          <div class="header-kicker">SHOP ADMIN</div>
          <div class="title">{{ $route.meta.title }}</div>
        </div>
        <div class="header-actions">
          <div class="admin-pill">管理员</div>
          <el-button text @click="logout">退出登录</el-button>
        </div>
      </el-header>
      <el-main class="content"><router-view /></el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()
function logout() {
  localStorage.removeItem('token')
  router.push('/login')
}
</script>

<style scoped>
.layout { min-height: 100%; background: #f6f1e8; }
.aside {
  position: relative;
  overflow: hidden;
  background:
    radial-gradient(circle at 70% 12%, rgba(226,192,120,.22), transparent 28%),
    linear-gradient(180deg, #214f40 0%, #17372e 100%);
  box-shadow: 20px 0 50px rgba(31,90,70,.14);
}
.aside::after {
  content: '';
  position: absolute;
  inset: auto -40px -70px 36px;
  height: 170px;
  border: 1px solid rgba(226,192,120,.2);
  border-radius: 999px;
  transform: rotate(-18deg);
}
.logo-block { position: relative; z-index: 1; display: flex; align-items: center; gap: 12px; padding: 24px 20px 22px; }
.logo-seal { display: grid; place-items: center; width: 46px; height: 46px; color: #17372e; background: #e2c078; border-radius: 16px; font-size: 20px; font-weight: 900; }
.logo-title { color: #fff2d8; font-size: 18px; font-weight: 900; }
.logo-subtitle { margin-top: 3px; color: rgba(255,242,216,.58); font-size: 11px; letter-spacing: 2px; text-transform: uppercase; }
.nav-menu { position: relative; z-index: 1; border-right: 0; padding: 8px 14px; background: transparent; }
.nav-menu :deep(.el-menu-item) { height: 48px; margin: 8px 0; border-radius: 16px; color: rgba(255,242,216,.72); font-weight: 800; }
.nav-menu :deep(.el-menu-item:hover) { color: #fff2d8; background: rgba(255,250,242,.08); }
.nav-menu :deep(.el-menu-item.is-active) { color: #17372e; background: linear-gradient(135deg, #f1d28d, #fff2d8); box-shadow: 0 12px 22px rgba(0,0,0,.16); }
.aside-footer { position: absolute; left: 20px; right: 20px; bottom: 20px; z-index: 1; padding: 14px; border: 1px solid rgba(226,192,120,.24); border-radius: 18px; background: rgba(255,250,242,.08); }
.aside-footer span { display: block; color: #e2c078; font-size: 10px; font-weight: 900; letter-spacing: 2px; }
.aside-footer strong { display: block; margin-top: 6px; color: rgba(255,242,216,.82); font-size: 13px; }
.main-wrap { min-width: 0; }
.header { height: 78px; display: flex; align-items: center; justify-content: space-between; padding: 0 28px; background: rgba(255,250,242,.78); border-bottom: 1px solid #eadbc4; backdrop-filter: blur(14px); }
.header-kicker { color: #c39a55; font-size: 11px; letter-spacing: 3px; font-weight: 900; }
.title { margin-top: 4px; color: #17372e; font-size: 22px; font-weight: 900; }
.header-actions { display: flex; align-items: center; gap: 12px; }
.admin-pill { padding: 8px 14px; border-radius: 999px; background: #eef4f0; color: #1f5a46; font-size: 13px; font-weight: 900; }
.content { padding: 28px; }
</style>
