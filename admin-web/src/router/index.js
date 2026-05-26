import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: () => import('@/layout/MainLayout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'category',
        name: 'Category',
        component: () => import('@/views/category/List.vue'),
        meta: { title: '分类管理' }
      },
      {
        path: 'product',
        name: 'Product',
        component: () => import('@/views/product/List.vue'),
        meta: { title: '商品管理' }
      },
      {
        path: 'product/create',
        name: 'ProductCreate',
        component: () => import('@/views/product/Form.vue'),
        meta: { title: '新增商品' }
      },
      {
        path: 'product/:id/edit',
        name: 'ProductEdit',
        component: () => import('@/views/product/Form.vue'),
        meta: { title: '编辑商品' }
      },
      {
        path: 'order',
        name: 'Order',
        component: () => import('@/views/order/List.vue'),
        meta: { title: '订单管理' }
      },
      {
        path: 'order/:id',
        name: 'OrderDetail',
        component: () => import('@/views/order/Detail.vue'),
        meta: { title: '订单详情' }
      },
      {
        path: 'address',
        name: 'Address',
        component: () => import('@/views/address/List.vue'),
        meta: { title: '配送区域' }
      },
      {
        path: 'delivery',
        name: 'Delivery',
        component: () => import('@/views/delivery/List.vue'),
        meta: { title: '配送员管理' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
