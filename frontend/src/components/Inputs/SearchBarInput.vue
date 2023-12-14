<script setup>
	import { ref, watch } from "vue";
	import { RouterLink, useRouter } from "vue-router";
	import { getListingsSearch, getListingsSuggestions } from "@/utils/axios";
	import { useProductStore } from "@/stores/products";

	const productsList = useProductStore();
	const router = useRouter();

	const searchInput = ref("");
	const suggestions = ref([]);
	const suggestionList = ref([]);
	const suggestionListBox = ref(false);
	const activeIndex = ref(-1);
	const refInput = ref(null);
	let searchTimeout;

	const handleEsc = () => {
		suggestionListBox.value = false;
		refInput.value.blur();
	};

	const handleEnter = async () => {
		if (activeIndex.value !== -1) {
			searchInput.value = "";
			router.push(`/listings/${suggestions.value[activeIndex.value].id}`);
		} else {
			const response = await getListingsSearch({
				name: searchInput.value,
			});
			searchInput.value = "";
			if (response.error) {
				// error
			} else {
				//funcion de main para cambiar el array de productos
				// searchUpdate(response.data.content);
				productsList.update(response.data.content);
			}
		}
	};

	const handleArrowDown = () => {
		const suggestionItems = suggestionList.value.querySelectorAll("li");

		if (activeIndex.value < suggestionItems.length - 1) {
			activeIndex.value++;
			focusSuggestion(activeIndex.value);
		} else {
			activeIndex.value = -1;
			focusSuggestion(activeIndex.value);
		}
	};

	const handleArrowUp = () => {
		const suggestionItems = suggestionList.value.querySelectorAll("li");

		if (activeIndex.value > -1) {
			activeIndex.value--;
			focusSuggestion(activeIndex.value);
		} else {
			activeIndex.value = suggestionItems.length - 1;
			focusSuggestion(activeIndex.value);
		}
	};

	const focusSuggestion = index => {
		const suggestionItems = suggestionList.value.querySelectorAll("li");
		suggestionItems.forEach((item, i) => {
			if (i === index) {
				item.classList.add("bg-[#efefef]");
			} else {
				item.classList.remove("bg-[#efefef]");
			}
		});
	};

	const getSuggestions = async query => {
		const response = await getListingsSuggestions(query);
		if (response.error) {
			// error
		} else {
			suggestions.value = response.data;
		}
	};

	const onSearchInput = () => {
		clearTimeout(searchTimeout);
		searchTimeout = setTimeout(() => {
			getSuggestions(searchInput.value);
		}, 1000);
	};

	watch(searchInput, () => {
		if (searchInput.value.length > 0) {
			onSearchInput();
		} else {
			suggestions.value = [];
		}
	});
</script>

<template>
	<input
		type="text"
		ref="refInput"
		id="icon"
		name="searchbox"
		class="peer block w-96 rounded-md border-gray-200 bg-white/70 px-4 py-2 pl-11 text-sm font-normal shadow-sm placeholder:italic placeholder:text-gray-500 focus:z-10 focus:border-gray-200 focus:bg-white focus:ring-0"
		placeholder="Search..."
		@keydown.down="handleArrowDown"
		@keydown.up="handleArrowUp"
		@keydown.esc="handleEsc"
		@keydown.enter="handleEnter"
		@focusin="suggestionListBox = true"
		@focusout="suggestionListBox = false"
		v-model="searchInput"
		autocomplete="off"
	/>
	<ul
		class="peer-focus:border-3 absolute top-[33px] w-full cursor-default list-none flex-col items-center rounded-b-md border-b border-l border-r border-gray-200 bg-white px-1 py-1 font-light shadow-2xl peer-focus:border-t-gray-200"
		ref="suggestionList"
		v-if="suggestionListBox && suggestions.length > 0"
	>
		<li
			v-for="(item, index) in suggestions"
			:key="index"
			class="w-full truncate rounded-sm first:border-t hover:bg-[#efefef]"
		>
			<router-link
				class="block h-full w-full px-10 py-1"
				:to="'/listings/' + item.id"
				@click="searchInput = ''"
			>
				{{ item.name }}
			</router-link>
		</li>
	</ul>
</template>
