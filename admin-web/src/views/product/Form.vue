<template>
  <div class="page-shell product-form-page">
    <div class="form-hero">
      <div>
        <button class="back-link" @click="$router.back()">← 返回商品列表</button>
        <div class="page-kicker">PRODUCT EDITOR</div>
        <h1>{{ isEdit ? '编辑商品' : '新增商品' }}</h1>
        <p>维护商品封面、价格、库存和推荐状态，保持小程序端展示统一。</p>
      </div>
      <div class="preview-card">
        <el-image v-if="form.coverImage" :src="form.coverImage" fit="cover" />
        <div v-else class="preview-empty">茶</div>
        <span>{{ form.name || '新品甜品' }}</span>
      </div>
    </div>

    <el-card class="form-card">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="88px">
        <el-row :gutter="22">
          <el-col :xs="24" :md="14">
            <el-form-item label="名称" prop="name">
              <el-input v-model="form.name" placeholder="如：桂花米乳酪酪" />
            </el-form-item>
            <el-form-item label="分类" prop="categoryId">
              <el-select v-model="form.categoryId" placeholder="选择分类" class="full-select">
                <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
              </el-select>
            </el-form-item>
            <el-form-item label="描述">
              <el-input v-model="form.description" type="textarea" :rows="5" placeholder="描述口味、原料或推荐语" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="10">
            <el-form-item label="封面图">
              <div class="upload-box">
                <el-upload :action="uploadUrl" :headers="uploadHeaders" :on-success="onCoverSuccess" :show-file-list="false" accept="image/*">
                  <el-button>上传封面</el-button>
                </el-upload>
                <span>建议使用方形或 4:3 商品图</span>
              </div>
            </el-form-item>
            <el-form-item label="是否推荐">
              <el-switch v-model="form.isRecommend" :active-value="1" :inactive-value="0" />
            </el-form-item>
          </el-col>
        </el-row>

        <div class="price-grid">
          <el-form-item label="售价" prop="price">
            <el-input-number v-model="form.price" :precision="2" :min="0" />
          </el-form-item>
          <el-form-item label="原价">
            <el-input-number v-model="form.originPrice" :precision="2" :min="0" />
          </el-form-item>
          <el-form-item label="库存" prop="stock">
            <el-input-number v-model="form.stock" :min="0" />
          </el-form-item>
        </div>

        <el-form-item class="form-actions">
          <el-button type="primary" @click="doSave" :loading="saving">保存商品</el-button>
          <el-button @click="$router.back()">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { saveProduct, updateProduct, getProductDetail } from '@/api/product'
import { getCategoryList } from '@/api/category'

const route = useRoute()
const router = useRouter()
const isEdit = computed(() => !!route.params.id)
const saving = ref(false)
const formRef = ref(null)
const categories = ref([])
const form = reactive({
  name: '', categoryId: null, coverImage: '', description: '',
  price: 0, originPrice: 0, stock: 0, unit: '份', minStock: 0,
  status: 1, sortOrder: 0, isRecommend: 0,
  images: [], skuList: []
})
const rules = {
  name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
  price: [{ required: true, message: '请输入售价', trigger: 'blur' }],
  stock: [{ required: true, message: '请输入库存', trigger: 'blur' }]
}
const uploadUrl = '/api/admin/product/upload'
const uploadHeaders = computed(() => ({ token: localStorage.getItem('token') }))

onMounted(async () => {
  try { categories.value = await getCategoryList() } catch { /* */ }
  if (isEdit.value) {
    try {
      const data = await getProductDetail(route.params.id)
      Object.assign(form, { ...data, images: data.images || [], categoryId: data.categoryId })
    } catch { /* */ }
  }
})

function onCoverSuccess(res) {
  if (res.code === 200) form.coverImage = res.data
}

async function doSave() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  saving.value = true
  try {
    if (isEdit.value) await updateProduct(route.params.id, form)
    else await saveProduct(form)
    ElMessage.success(isEdit.value ? '更新成功' : '新增成功')
    router.push('/product')
  } catch { /* */ }
  finally { saving.value = false }
}
</script>

<style scoped>
.form-hero { display: flex; align-items: flex-end; justify-content: space-between; gap: 24px; padding: 28px; border-radius: 30px; background: radial-gradient(circle at 85% 18%, rgba(195,154,85,.2), transparent 34%), #fffaf2; border: 1px solid #eadbc4; box-shadow: 0 16px 36px rgba(83,59,33,.08); }
.back-link { margin-bottom: 18px; border: 0; color: #1f5a46; background: transparent; font-weight: 900; cursor: pointer; }
.form-hero h1 { margin: 8px 0 10px; color: #17372e; font-size: 34px; font-weight: 900; }
.form-hero p { color: #8d8271; line-height: 1.8; }
.preview-card { width: 168px; padding: 12px; border-radius: 24px; background: #f6f1e8; border: 1px solid #eadbc4; }
.preview-card .el-image, .preview-empty { width: 144px; height: 112px; border-radius: 18px; overflow: hidden; }
.preview-empty { display: grid; place-items: center; color: #fff2d8; background: linear-gradient(135deg, #1f5a46, #17372e); font-size: 36px; font-weight: 900; }
.preview-card span { display: block; margin-top: 10px; color: #17372e; font-weight: 900; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.form-card { margin-top: 18px; }
.full-select { width: 100%; }
.upload-box { display: flex; align-items: center; gap: 12px; flex-wrap: wrap; }
.upload-box span { color: #9a8d78; font-size: 13px; }
.price-grid { display: grid; grid-template-columns: repeat(3, minmax(0, 1fr)); gap: 12px; }
.form-actions { margin-bottom: 0; }
@media (max-width: 900px) { .form-hero { align-items: flex-start; flex-direction: column; } .price-grid { grid-template-columns: 1fr; } }
</style>
