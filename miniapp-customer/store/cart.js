import { defineStore } from 'pinia'
import { getCartList, addToCart, updateCartItem, removeCartItems } from '@/api/index'

export const useCartStore = defineStore('cart', {
  state: () => ({
    items: [],
    totalCount: 0
  }),
  actions: {
    async fetch() {
      try {
        const list = await getCartList()
        this.items = list || []
        this.totalCount = this.items.filter(i => i.checked).reduce((s, i) => s + i.quantity, 0)
      } catch { /* */ }
    },
    async add(productId, skuInfo, quantity) {
      await addToCart({ productId, skuInfo, quantity })
      await this.fetch()
    },
    async update(id, quantity, checked) {
      await updateCartItem({ id, quantity, checked })
      await this.fetch()
    },
    async remove(ids) {
      await removeCartItems(ids)
      await this.fetch()
    }
  }
})
