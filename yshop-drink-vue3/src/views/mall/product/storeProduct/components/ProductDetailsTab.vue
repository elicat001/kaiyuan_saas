<template>
  <div class="product-details-tab">
    <el-form-item label="产品描述">
      <vue-ueditor-wrap
        v-model="formData.description"
        :config="editorConfig"
        @before-init="addCustomDialog"
        style="width: 90%;"
      />
    </el-form-item>
  </div>
</template>

<script setup lang="ts">
import VueUeditorWrap from 'vue-ueditor-wrap'

defineOptions({ name: 'ProductDetailsTab' })

const props = defineProps<{
  modelValue: Record<string, any>
  editorConfig: Record<string, any>
}>()

const emit = defineEmits(['update:modelValue'])

const formData = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

const addCustomDialog = (editorId: string) => {
  window.UE.registerUI('yshop', function (editor: any, uiName: string) {
    var dialog = new window.UE.ui.Dialog({
      iframeUrl: '/bindPicture?name=yshop&editor=' + editorId,
      cssRules: 'width:1200px;height:500px;',
      editor: editor,
      name: uiName,
      title: '上传图片'
    })

    var btn = new window.UE.ui.Button({
      name: 'yshop',
      title: '上传图片',
      cssRules: '',
      onclick: function () {
        dialog.render()
        dialog.open()
      }
    })
    return btn
  }, 38)
}
</script>

<style scoped>
.product-details-tab {
  width: 100%;
}
</style>
