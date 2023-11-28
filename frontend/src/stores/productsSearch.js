import { defineStore } from "pinia";
import { ref } from "vue";

export const useProductsSearchStore = defineStore("productsSearch", () => {
	const search = ref("");
	const categoryId = ref(0);
	return {
		search,
		categoryId,
	};
});
