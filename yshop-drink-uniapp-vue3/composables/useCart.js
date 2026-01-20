/**
 * 购物车 Composable
 * 统一管理购物车逻辑，解决代码重复问题
 */
import { ref, computed } from 'vue'

// 购物车数据（响应式）
const cart = ref([])

// 初始化购物车（从本地存储加载）
const initCart = () => {
  try {
    const savedCart = uni.getStorageSync('cart')
    if (savedCart && Array.isArray(savedCart)) {
      cart.value = savedCart
    }
  } catch (e) {
    console.error('加载购物车失败:', e)
    cart.value = []
  }
}

// 保存购物车到本地存储
const saveCart = () => {
  try {
    uni.setStorageSync('cart', JSON.parse(JSON.stringify(cart.value)))
  } catch (e) {
    console.error('保存购物车失败:', e)
  }
}

// 清空购物车
const clearCart = () => {
  cart.value = []
  saveCart()
}

// 添加商品到购物车
const addToCart = (product) => {
  const existingIndex = cart.value.findIndex(
    item => item.id === product.id && item.spec === product.spec
  )

  if (existingIndex > -1) {
    cart.value[existingIndex].number += 1
  } else {
    cart.value.push({
      ...product,
      number: 1
    })
  }
  saveCart()
}

// 从购物车移除商品
const removeFromCart = (productId, spec) => {
  const existingIndex = cart.value.findIndex(
    item => item.id === productId && item.spec === spec
  )

  if (existingIndex > -1) {
    if (cart.value[existingIndex].number > 1) {
      cart.value[existingIndex].number -= 1
    } else {
      cart.value.splice(existingIndex, 1)
    }
    saveCart()
  }
}

// 删除购物车商品
const deleteFromCart = (productId, spec) => {
  const existingIndex = cart.value.findIndex(
    item => item.id === productId && item.spec === spec
  )

  if (existingIndex > -1) {
    cart.value.splice(existingIndex, 1)
    saveCart()
  }
}

// 更新商品数量
const updateQuantity = (productId, spec, quantity) => {
  const existingIndex = cart.value.findIndex(
    item => item.id === productId && item.spec === spec
  )

  if (existingIndex > -1) {
    if (quantity <= 0) {
      cart.value.splice(existingIndex, 1)
    } else {
      cart.value[existingIndex].number = quantity
    }
    saveCart()
  }
}

// 获取商品在购物车中的数量
const getCartQuantity = computed(() => {
  return (productId, spec = '') => {
    const item = cart.value.find(
      item => item.id === productId && (spec === '' || item.spec === spec)
    )
    return item ? item.number : 0
  }
})

// 获取指定商品ID的总数量（所有规格）
const getProductTotalQuantity = computed(() => {
  return (productId) => {
    return cart.value
      .filter(item => item.id === productId)
      .reduce((acc, cur) => acc + cur.number, 0)
  }
})

// 获取指定分类的商品总数量
const getCategoryTotalQuantity = computed(() => {
  return (categoryId) => {
    return cart.value
      .filter(item => item.cate_id === categoryId)
      .reduce((acc, cur) => acc + cur.number, 0)
  }
})

// 购物车商品总数
const totalQuantity = computed(() => {
  return cart.value.reduce((acc, cur) => acc + cur.number, 0)
})

// 购物车总价
const totalPrice = computed(() => {
  return cart.value.reduce((acc, cur) => acc + cur.price * cur.number, 0)
})

// 购物车是否为空
const isEmpty = computed(() => {
  return cart.value.length === 0
})

// 导出 composable
export function useCart() {
  // 首次使用时初始化
  if (cart.value.length === 0) {
    initCart()
  }

  return {
    // 状态
    cart,

    // 计算属性
    totalQuantity,
    totalPrice,
    isEmpty,
    getCartQuantity,
    getProductTotalQuantity,
    getCategoryTotalQuantity,

    // 方法
    initCart,
    saveCart,
    clearCart,
    addToCart,
    removeFromCart,
    deleteFromCart,
    updateQuantity
  }
}

export default useCart
