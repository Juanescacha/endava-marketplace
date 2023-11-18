<script setup>
	import { onBeforeMount, ref, nextTick } from "vue";
	import {
		Disclosure,
		DisclosureButton,
		DisclosurePanel,
	} from "@headlessui/vue";
	import { ChevronUpIcon, PencilSquareIcon } from "@heroicons/vue/20/solid";
	import {
		getAllCategories,
		postCategory,
		patchCategoryName,
		patchEnableCategory,
		patchDisableCategory,
	} from "@/utils/axios";

	// components
	import TableBase from "@/components/TableBase.vue";

	const categories = ref([]);
	const columns = ["ID", "Name", "Status", "Actions"];
	const inputName = ref("");
	const inputRef = ref([]);

	onBeforeMount(() => {
		fetchCategories();
	});

	const fetchCategories = async () => {
		const response = await getAllCategories();
		if (response.error) {
			// error
		} else {
			categories.value = response.data;
			initializeRefsArray();
		}
	};

	const initializeRefsArray = () => {
		inputRef.value = Array(categories.value.length).fill(null);
	};

	const toggleRename = async (category, index) => {
		category.editable = true;
		await nextTick();
		inputRef.value[index].focus();
	};

	const handleRename = category => {
		category.editable = false;
		patchCategoryName(category.id, category.name);
	};

	const handleEnable = category => {
		patchEnableCategory(category.id);
		category.active = true;
	};

	const handleToggle = category => {
		category.active ? handleDisable(category) : handleEnable(category);
		category.active = !category.active;
	};

	const handleDisable = category => {
		patchDisableCategory(category.id);
		category.active = false;
	};

	const handlePost = name => {
		postCategory(name);
	};
</script>

<template>
	<div class="mx-auto max-w-4xl">
		<div class="my-6">
			<h2 class="mr-3 inline text-3xl">Categories</h2>
			<Disclosure v-slot="{ open: filtersOpen }">
				<DisclosureButton class="py-2">
					<ChevronUpIcon
						:class="
							filtersOpen
								? 'rotate-180 transform'
								: 'rotate-90 transform'
						"
						class="h-6 w-6"
					/>
				</DisclosureButton>
				<DisclosurePanel class="text-gray-500">
					<form class="my-7 flex gap-3">
						<label
							class="sr-only"
							for="categoryName"
						>
							Category Name
						</label>
						<input
							type="text"
							id="categoryName"
							placeholder="Category Name"
							v-model="inputName"
							class="h-full w-48 rounded border border-zinc-400 px-2 py-3 text-black placeholder:text-gray-400"
						/>
						<button
							type="submit"
							aria-label="Add category"
							class="endava rounded-full px-10 py-3"
							@click.prevent="handlePost(inputName)"
						>
							Add
						</button>
					</form>
				</DisclosurePanel>
			</Disclosure>
		</div>
		<table-base
			:columns="columns"
			v-if="categories.length > 0"
		>
			<tr
				v-for="(category, index) in categories"
				class="h-12"
			>
				<td class="px-4 py-2 font-normal">{{ category.id }}</td>
				<td class="px-4 py-2 font-normal">
					<input
						type="text"
						:id="category.id"
						v-model="category.name"
						:readonly="!category.editable"
						:disabled="!category.editable"
						class="read-only:select-none read-only:border-transparent read-only:focus:ring-transparent"
						@keypress.enter="handleRename(category)"
						@keydown.esc="handleRename(category)"
						@blur="handleRename(category)"
						:ref="el => (inputRef[index] = el)"
					/>
				</td>
				<td class="px-4 py-2 font-normal">
					<label class="relative block h-6 w-12 text-[0.1px]">
						toggle status
						<input
							type="checkbox"
							class="peer h-0 w-0 opacity-0"
							:checked="category.active"
							@change="handleToggle(category)"
						/>
						<span
							class="absolute bottom-0 left-0 right-0 top-0 cursor-pointer rounded-full bg-red-500 transition-colors before:absolute before:bottom-[0.1875rem] before:left-[0.25rem] before:h-[1.125rem] before:w-[1.125rem] before:rounded-full before:bg-white before:transition-all before:content-[''] peer-checked:bg-green-500 peer-checked:before:translate-x-[1.375rem] peer-focus:outline peer-focus:outline-[3px] peer-focus:outline-black"
						></span>
					</label>
				</td>
				<td class="px-4 py-2 text-center font-normal text-white">
					<button
						type="button"
						class="inline-block rounded-sm bg-blue-500 p-1 hover:bg-blue-400 focus:outline focus:outline-[3px] focus:outline-black active:bg-blue-300 disabled:bg-gray-200"
						@click="toggleRename(category, index)"
					>
						<PencilSquareIcon class="h-5" />
					</button>
				</td>
			</tr>
		</table-base>
	</div>
</template>
