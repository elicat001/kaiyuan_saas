<template>
  <div class="basic-info-tab">
    <el-form-item label="展示店铺" prop="shopId">
      <el-select
        v-model="formData.shopId"
        placeholder="选择店铺"
        @change="handleShopChange"
      >
        <el-option
          v-for="item in shopList"
          :key="item.id"
          :label="item.name"
          :value="item.id"
        />
      </el-select>
    </el-form-item>
    <el-form-item label="商品名称" prop="store_name">
      <el-input v-model="formData.store_name" class="input-width" placeholder="请输入商品名称" />
    </el-form-item>
    <el-form-item label="商品分类" prop="cate_id">
      <el-select
        v-model="formData.cate_id"
        placeholder="选择分类"
      >
        <el-option
          v-for="item in categoryTree"
          :key="item.id"
          :label="item.name"
          :value="item.id"
        />
      </el-select>
    </el-form-item>
    <el-form-item label="关键字" prop="keyword">
      <el-input v-model="formData.keyword" class="input-width" placeholder="请输入关键字" />
    </el-form-item>
    <el-form-item label="单位名" prop="unit_name">
      <el-input v-model="formData.unit_name" class="input-width" placeholder="请输入单位名" />
    </el-form-item>
    <el-form-item label="商品价格" prop="price">
      <el-input v-model="formData.price" class="input-width" placeholder="请输入商品价格" />
    </el-form-item>
    <el-form-item label="市场价" prop="otPrice">
      <el-input v-model="formData.otPrice" class="input-width" placeholder="请输入市场价" />
    </el-form-item>
    <el-form-item label="库存" prop="stock">
      <el-input v-model="formData.stock" class="input-width" placeholder="请输入库存" />
    </el-form-item>
    <el-form-item label="封面图" prop="image">
      <Materials v-model="formData.image" num="1" type="image" />
    </el-form-item>
    <el-form-item label="轮播图" prop="slider_image">
      <Materials v-model="formData.slider_image" num="5" type="image" />
    </el-form-item>
    <el-form-item label="商品状态" prop="is_show">
      <el-radio-group v-model="formData.is_show">
        <el-radio :label="1">上架</el-radio>
        <el-radio :label="0">下架</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="商品简介" prop="store_info">
      <el-input type="textarea" rows="5" v-model="formData.store_info" placeholder="请输入商品简介" />
    </el-form-item>
  </div>
</template>

<script setup lang="ts">
import Materials from '@/views/mall/shop/materials/index.vue'

defineOptions({ name: 'BasicInfoTab' })

const props = defineProps<{
  modelValue: Record<string, any>
  shopList: any[]
  categoryTree: any[]
}>()

const emit = defineEmits(['update:modelValue', 'shop-change'])

const formData = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

const handleShopChange = (val: any) => {
  emit('shop-change', val)
}
</script>

<style scoped>
.input-width {
  width: 380px;
}
</style>
