<template>
  <div class="marketing-tab">
    <el-form-item label="获得积分" prop="give_integral">
      <el-input v-model="formData.give_integral" class="input-width" placeholder="请输入获得积分" />
    </el-form-item>
  </div>
</template>

<script setup lang="ts">
defineOptions({ name: 'MarketingTab' })

const props = defineProps<{
  modelValue: Record<string, any>
}>()

const emit = defineEmits(['update:modelValue'])

const formData = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})
</script>

<style scoped>
.input-width {
  width: 380px;
}
</style>
