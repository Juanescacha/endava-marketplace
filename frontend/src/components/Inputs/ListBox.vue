<script setup>
	import { ref, computed, watch, onBeforeMount } from "vue";
	import { useProductStore } from "@/stores/products";
	import { getListingsSearch } from "@/utils/axios";
	import {
		Listbox,
		ListboxLabel,
		ListboxButton,
		ListboxOptions,
		ListboxOption,
	} from "@headlessui/vue";
	import { CheckIcon, ChevronUpDownIcon } from "@heroicons/vue/20/solid";
	import { getAllCategories } from "@/utils/axios";
	import { useProductsSearchStore } from "@/stores/productsSearch";

	const productsList = useProductStore();
	const productsSearch = useProductsSearchStore();

	const options = ref([]);
	const activeCategories = computed(() => {
		return options.value.filter(category => category.active);
	});
	const selectedOption = ref({ name: "All Categories", id: 0 });

	const categorySearch = async () => {
		const response = await getListingsSearch({
			category: productsSearch.categoryId,
		});
		if (response.error) {
		} else {
			productsList.update(response.data.content);
		}
	};

	const fetchCategories = async () => {
		const response = await getAllCategories();
		if (response.error) {
			// error
		} else {
			options.value = response.data;
		}
	};

	onBeforeMount(async () => {
		await fetchCategories();
	});

	watch(selectedOption, () => {
		productsSearch.categoryId = selectedOption.value.id;
		categorySearch();
	});
</script>

<template>
	<Listbox
		v-model="selectedOption"
		class="w-52"
	>
		<div class="relative">
			<ListboxButton
				class="relative w-full cursor-default rounded-lg border border-gray-200 bg-white py-2 pl-3 pr-10 text-left text-gray-500 shadow-sm focus:border-blue-500 focus:outline-none focus:ring-1 focus:ring-blue-500 active:border-blue-500 active:ring-1 active:ring-blue-500 sm:text-sm"
			>
				<span class="block truncate">{{ selectedOption.name }}</span>
				<span
					class="pointer-events-none absolute inset-y-0 right-0 flex items-center pr-2"
				>
					<ChevronUpDownIcon
						class="h-5 w-5 text-gray-400"
						aria-hidden="true"
					/>
				</span>
			</ListboxButton>

			<transition
				leave-active-class="transition duration-100 ease-in"
				leave-from-class="opacity-100"
				leave-to-class="opacity-0"
			>
				<ListboxOptions
					class="absolute mt-1 max-h-60 w-full overflow-auto rounded-md bg-white text-base shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm"
				>
					<ListboxOption
						v-slot="{ active, selected }"
						v-for="category in activeCategories"
						:key="category.id"
						:value="category"
						as="template"
					>
						<li
							:class="[
								active
									? 'bg-orange-200 text-gray-900'
									: 'text-gray-500',
								'relative cursor-default select-none py-2 pl-10 pr-4',
							]"
						>
							<span
								:class="[
									selected
										? 'font-medium text-black'
										: 'font-normal',
									'block truncate',
								]"
								>{{ category.name }}</span
							>
							<span
								v-if="selected"
								class="absolute inset-y-0 left-0 flex items-center pl-3 text-orange-500"
							>
								<CheckIcon
									class="h-5 w-5"
									aria-hidden="true"
								/>
							</span>
						</li>
					</ListboxOption>
				</ListboxOptions>
			</transition>
		</div>
	</Listbox>
</template>
