<template>
  <div>
    <svg-icon icon-class="question" @click="goto"/>
  </div>
</template>

<script>
export default {
  name: 'LuckinCRMDoc',
  data() {
    return {
      url: 'http://doc.luckin.xyz'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>