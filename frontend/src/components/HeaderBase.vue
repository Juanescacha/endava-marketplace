<script setup>
	import { ref } from "vue";
	import { useRouter } from "vue-router";
	import { suggestions } from "../constants";
	import {
		BellIcon,
		MagnifyingGlassIcon,
		BookOpenIcon,
		ListBulletIcon,
		Cog8ToothIcon,
	} from "@heroicons/vue/24/outline";
	import { CreditCardIcon } from "@heroicons/vue/24/solid";

	const router = useRouter();

	const searchInput = ref("");
	const categoryInput = ref("");
	const suggestionList = ref([]);
	const suggestionListBox = ref(false);
	const activeIndex = ref(-1);

	const handleEsc = () => {
		suggestionListBox.value = false;
	};

	const handleEnter = () => {
		if (activeIndex.value !== -1) {
			// acceder a product page
		} else {
			// realizar busqueda completa
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
</script>

<template>
	<header
		class="fixed top-0 z-50 flex w-full flex-wrap border-b bg-white/70 py-2.5 text-sm backdrop-blur-md sm:flex-nowrap sm:justify-start sm:py-4"
	>
		<nav
			class="mx-auto flex w-full max-w-7xl basis-full items-center px-4 sm:px-6 lg:px-8"
			aria-label="Global"
		>
			<div class="mr-5 md:mr-8">
				<router-link
					class="flex-none text-xl font-semibold"
					to="/"
					aria-label="Brand"
					><img
						src="../assets/endava-logo.png"
						alt="logo"
						class="w-32"
				/></router-link>
			</div>
			<div
				class="ml-auto flex w-full items-center justify-end sm:order-3 sm:justify-end sm:gap-x-3"
			>
				<div class="sm:hidden">
					<button
						type="button"
						class="mr-2 inline-flex h-[2.375rem] w-[2.375rem] flex-shrink-0 items-center justify-center gap-2 rounded-full bg-white align-middle text-xs font-medium text-gray-700 transition-all hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-endava-400 focus:ring-offset-2 focus:ring-offset-white"
					>
						<MagnifyingGlassIcon class="h-5 w-5 text-current" />
					</button>
				</div>
				<div class="hidden sm:block">
					<label
						for="icon"
						class="sr-only"
						>Search</label
					>
					<div class="relative">
						<div
							class="pointer-events-none absolute inset-y-0 left-0 z-20 flex items-center pl-4"
						>
							<MagnifyingGlassIcon
								class="h-5 w-5 text-gray-400"
							/>
						</div>
						<input
							type="text"
							id="icon"
							name="searchbox"
							class="peer block w-96 rounded-md border-gray-200 bg-white/70 px-4 py-2 pl-11 text-sm font-normal shadow-sm placeholder:italic placeholder:text-gray-500 focus:z-10 focus:border-gray-200 focus:bg-white focus:ring-0"
							placeholder="Search..."
							@keydown.down="handleArrowDown"
							@keydown.up="handleArrowUp"
							@keydown.esc="handleEsc"
							@keydown.enter="handleEnter"
							v-model="searchInput"
							@focusin="suggestionListBox = true"
							@focusout="suggestionListBox = false"
							autocomplete="off"
						/>
						<ul
							class="peer-focus:border-3 absolute top-[33px] flex w-full cursor-default list-none flex-col items-center divide-y rounded-b-md border-b border-l border-r border-gray-200 bg-white px-2 py-2 font-light shadow-2xl peer-focus:border-t-gray-200"
							ref="suggestionList"
							:class="{
								block: suggestionListBox,
								hidden: !suggestionListBox,
							}"
						>
							<li
								v-for="(item, index) in suggestions"
								:key="index"
								class="w-full rounded-sm px-10 py-1 first:border-t last:rounded-b-md hover:bg-[#efefef]"
							>
								{{ item.title }}
							</li>
						</ul>
					</div>
				</div>
				<div class="">
					<select
						id="categories"
						class="hidden w-52 rounded-md border-gray-200 py-2 pl-6 pr-11 text-sm font-light shadow-sm focus:z-10 focus:border-gray-200 focus:ring-gray-200 sm:block"
						:class="{
							'text-gray-500': categoryInput === '',
							'bg-white/20': categoryInput === '',
							'text-black': categoryInput !== '',
							'bg-white': categoryInput !== '',
						}"
						v-model="categoryInput"
					>
						<option
							value=""
							disabled
							selected
							hidden
						>
							Categories
						</option>
						<option value="1">Clothes</option>
						<option value="2">Technology</option>
						<option value="3">Vehicles</option>
					</select>
				</div>
				<div class=""></div>
				<div class="flex gap-4">
					<button
						type="button"
						class="endava inline-flex items-center justify-center gap-2 rounded-md border px-4 py-3 text-sm font-semibold text-white shadow-sm transition-all"
					>
						Sell
						<CreditCardIcon class="h-5 w-5" />
					</button>
				</div>

				<div class="flex flex-row items-center justify-end gap-2">
					<button
						type="button"
						class="inline-flex h-[2.375rem] w-[2.375rem] flex-shrink-0 items-center justify-center gap-2 rounded-full bg-white/5 align-middle text-xs font-medium text-gray-700 transition-all hover:bg-gray-100/50 hover:shadow-md active:bg-gray-200/50"
					>
						<BellIcon class="h-5 w-5 text-current" />
					</button>
					<button
						type="button"
						class="inline-flex h-[2.375rem] w-[2.375rem] flex-shrink-0 items-center justify-center gap-2 rounded-full bg-white/5 align-middle text-xs font-medium text-gray-700 transition-all hover:bg-gray-100/50 hover:shadow-md active:bg-gray-200/50"
						data-hs-offcanvas="#hs-offcanvas-right"
					>
						<BookOpenIcon class="h-5 w-5 text-current" />
					</button>

					<div
						class="hs-dropdown relative inline-flex"
						data-hs-dropdown-placement="bottom-right"
					>
						<button
							id="hs-dropdown-with-header"
							type="button"
							class="hs-dropdown-toggle inline-flex h-[2.375rem] w-[2.375rem] flex-shrink-0 items-center justify-center gap-2 rounded-full bg-white align-middle text-xs font-medium text-gray-700 transition-all hover:bg-gray-50 hover:shadow-md focus:outline-none focus:ring-2 focus:ring-endava-400 focus:ring-offset-2 focus:ring-offset-white active:brightness-75"
						>
							<img
								class="inline-block h-[2.375rem] w-[2.375rem] rounded-full"
								src="https://images.unsplash.com/photo-1568602471122-7832951cc4c5?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=facearea&facepad=2&w=320&h=320&q=80"
								alt="Image Description"
							/>
						</button>

						<div
							class="hs-dropdown-menu duration z-10 hidden min-w-[15rem] rounded-lg bg-white p-2 opacity-0 shadow-md transition-[opacity,margin] hs-dropdown-open:opacity-100"
							aria-labelledby="hs-dropdown-with-header"
						>
							<div
								class="-m-2 rounded-t-lg bg-gray-100 px-5 py-3"
							>
								<p class="text-sm text-gray-500">
									Signed in as
								</p>
								<p class="text-sm font-medium text-gray-800">
									Juan Esteban Camargo
								</p>
							</div>
							<div class="mt-2 py-2 first:pt-0 last:pb-0">
								<a
									class="flex items-center gap-x-3.5 rounded-md px-3 py-2 text-sm text-gray-800 hover:bg-gray-100 active:bg-gray-200"
									href="#"
								>
									<BellIcon
										class="h-5 w-5 flex-none text-current"
									/>
									Notifications
								</a>
								<a
									class="flex items-center gap-x-3.5 rounded-md px-3 py-2 text-sm text-gray-800 hover:bg-gray-100 active:bg-gray-200"
									href="#"
								>
									<ListBulletIcon
										class="h-5 w-5 flex-none text-current"
									/>
									Publications
								</a>
								<a
									class="flex items-center gap-x-3.5 rounded-md px-3 py-2 text-sm text-gray-800 hover:bg-gray-100 active:bg-gray-200"
									href="#"
								>
									<Cog8ToothIcon class="h-5 w-5 flex-none" />
									Profile
								</a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</nav>
	</header>
</template>
