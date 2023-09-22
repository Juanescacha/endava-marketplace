<template>
	<Header v-if="show" />
	<RouterView />
	<Footer v-if="show" />
</template>

<script setup>
import { ref, computed, onBeforeMount } from "vue";
import { useRoute } from "vue-router";
import Header from "./components/HeaderBase.vue";
import Footer from "./components/FooterBase.vue";

const $route = useRoute();
const currentPath = ref($route.path);
const show = computed(() => $route.path !== "/login" && $route.path !== "/404");

onBeforeMount(() => {
	currentPath.value = $route.path;
});
</script>
