<template>
  <div class="page-shell">
    <div class="page-heading">
      <div class="page-kicker">CATEGORY</div>
      <div class="page-title">分类管理</div>
    </div>

    <div class="toolbar">
      <div>
        <div class="toolbar-title">甜品分类</div>
        <div class="toolbar-subtitle">维护首页和点单页展示的分类顺序</div>
      </div>
      <el-button type="primary" @click="showDialog(null)">新增分类</el-button>
    </div>

    <el-table :data="tableData" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="名称" />
      <el-table-column prop="sortOrder" label="排序" width="100" />
      <el-table-column label="状态" width="110">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'" effect="light">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="170" align="right">
        <template #default="{ row }">
          <el-button size="small" @click="showDialog(row)">编辑</el-button>
          <el-button size="small" type="danger" plain @click="doDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="isEdit ? '编辑分类' : '新增分类'" v-model="dialogVisible" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="84px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="如：招牌鲜奶茶" />
        </el-form-item>
        <el-form-item label="图标" prop="icon">
          <div class="icon-upload">
            <el-upload :action="uploadUrl" :headers="uploadHeaders" :on-success="onIconSuccess" :show-file-list="false" accept="image/*">
              <el-button>上传图标</el-button>
            </el-upload>
            <el-image v-if="form.icon" :src="form.icon" class="icon-preview" fit="cover" />
            <span v-else>上传后自动保存图片地址</span>
          </div>
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="form.sortOrder" :min="0" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="doSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCategoryList, saveCategory, updateCategory, deleteCategory } from '@/api/category'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)
const form = reactive({ id: null, name: '', icon: '', sortOrder: 0, status: 1 })
const rules = { name: [{ required: true, message: '请输入名称', trigger: 'blur' }] }
const uploadUrl = '/api/admin/category/upload'
const uploadHeaders = computed(() => ({ token: localStorage.getItem('token') }))

onMounted(() => fetchData())

async function fetchData() {
  loading.value = true
  try { tableData.value = await getCategoryList() } catch { /* */ }
  finally { loading.value = false }
}

function showDialog(row) {
  isEdit.value = !!row
  if (row) Object.assign(form, row)
  else Object.assign(form, { id: null, name: '', icon: '', sortOrder: 0, status: 1 })
  dialogVisible.value = true
}

function onIconSuccess(res) {
  if (res.code === 200) form.icon = res.data
}

async function doSave() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  const data = { name: form.name, icon: form.icon, sortOrder: form.sortOrder, status: form.status, parentId: 0 }
  try {
    if (isEdit.value) await updateCategory(form.id, data)
    else await saveCategory(data)
    ElMessage.success(isEdit.value ? '更新成功' : '新增成功')
    dialogVisible.value = false
    fetchData()
  } catch { /* */ }
}

async function doDelete(id) {
  await ElMessageBox.confirm('确认删除？', '提示', { type: 'warning' })
  try { await deleteCategory(id); ElMessage.success('删除成功'); fetchData() } catch { /* */ }
}
</script>

<style scoped>
.toolbar-title { color: #17372e; font-size: 17px; font-weight: 900; }
.toolbar-subtitle { margin-top: 4px; color: #9a8d78; font-size: 13px; }
.icon-upload { display: flex; align-items: center; gap: 12px; flex-wrap: wrap; }
.icon-upload span { color: #9a8d78; font-size: 13px; }
.icon-preview { width: 54px; height: 54px; border-radius: 16px; background: #f6f1e8; border: 1px solid #eadbc4; }
</style>
