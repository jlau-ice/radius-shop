<template>
  <div class="page-shell dashboard-page">
    <div class="hero-card">
      <div>
        <div class="page-kicker">BUSINESS OVERVIEW</div>
        <h1>今日门店运营</h1>
        <p>用一个干净的视图盯住商品、订单和用户规模，后续接真实数据后可继续扩展销售趋势。</p>
      </div>
      <div class="hero-badge">
        <span>茶饮甜品</span>
        <strong>管理中</strong>
      </div>
    </div>

    <el-row :gutter="18" class="stats-grid">
      <el-col :xs="24" :sm="8" v-for="item in cards" :key="item.label">
        <el-card class="stat-card" :class="item.tone">
          <div class="stat-top">
            <span>{{ item.label }}</span>
            <i></i>
          </div>
          <div class="stat-value">{{ item.value }}</div>
          <div class="stat-label">{{ item.caption }}</div>
        </el-card>
      </el-col>
    </el-row>

    <div class="dashboard-grid">
      <el-card class="panel-card">
        <div class="panel-title">快捷入口</div>
        <div class="quick-links">
          <button @click="$router.push('/product/create')">新增商品</button>
          <button @click="$router.push('/order')">处理订单</button>
          <button @click="$router.push('/delivery')">配送员</button>
        </div>
      </el-card>
      <el-card class="panel-card tea-card">
        <div class="panel-title">本地测试提示</div>
        <p>管理端登录已走后端 mock 开关，正式上线前把 <strong>app.mock.admin-login</strong> 改为 false 即可使用数据库账号校验。</p>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getStats } from '@/api/stats'

const cards = ref([
  { label: '订单总数', value: 0, caption: '累计订单', tone: 'green' },
  { label: '商品数量', value: 0, caption: '在售与下架商品', tone: 'gold' },
  { label: '用户数量', value: 0, caption: '小程序用户', tone: 'cream' }
])

onMounted(async () => {
  try {
    const data = await getStats()
    cards.value[0].value = data.orderCount || 0
    cards.value[1].value = data.productCount || 0
    cards.value[2].value = data.userCount || 0
  } catch { /* ignore */ }
})
</script>

<style scoped>
.hero-card { display: flex; align-items: flex-end; justify-content: space-between; gap: 24px; min-height: 210px; padding: 30px; border-radius: 30px; color: #fff2d8; background: radial-gradient(circle at 82% 20%, rgba(226,192,120,.42), transparent 34%), linear-gradient(135deg, #214f40, #17372e); box-shadow: 0 24px 48px rgba(31,90,70,.2); }
.hero-card h1 { margin: 8px 0 10px; font-size: 38px; line-height: 1.1; font-weight: 900; }
.hero-card p { max-width: 560px; color: rgba(255,242,216,.72); line-height: 1.8; }
.hero-badge { display: grid; place-items: center; width: 128px; height: 128px; border: 1px solid rgba(255,242,216,.34); border-radius: 50%; background: rgba(255,250,242,.08); }
.hero-badge span { color: #e2c078; font-size: 12px; font-weight: 900; }
.hero-badge strong { color: #fff2d8; font-size: 22px; }
.stats-grid { margin-top: 18px; }
.stat-card { overflow: hidden; min-height: 154px; }
.stat-top { display: flex; align-items: center; justify-content: space-between; color: #8d8271; font-size: 13px; font-weight: 900; }
.stat-top i { width: 34px; height: 34px; border-radius: 14px; background: #eef4f0; }
.stat-value { margin-top: 20px; color: #17372e; font-size: 38px; font-weight: 900; letter-spacing: -1px; }
.stat-label { margin-top: 6px; color: #9a8d78; font-size: 13px; }
.stat-card.green .stat-top i { background: #dce9e3; }
.stat-card.gold .stat-top i { background: #f2dfb9; }
.stat-card.cream .stat-top i { background: #f4eadb; }
.dashboard-grid { display: grid; grid-template-columns: 1.1fr .9fr; gap: 18px; margin-top: 18px; }
.panel-card { min-height: 170px; }
.panel-title { color: #17372e; font-size: 18px; font-weight: 900; margin-bottom: 16px; }
.quick-links { display: flex; gap: 12px; flex-wrap: wrap; }
.quick-links button { padding: 13px 18px; border: 0; border-radius: 16px; color: #1f5a46; background: #eef4f0; font-weight: 900; cursor: pointer; }
.quick-links button:hover { color: #fff2d8; background: #1f5a46; }
.tea-card p { color: #796d5d; line-height: 1.9; }
.tea-card strong { color: #1f5a46; }
@media (max-width: 900px) { .dashboard-grid { grid-template-columns: 1fr; } .hero-card { align-items: flex-start; flex-direction: column; } }
</style>
