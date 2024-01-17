<script setup>
	import { RouterLink } from "vue-router";
	import { ChevronDownIcon, FunnelIcon } from "@heroicons/vue/24/solid";
	import { onBeforeMount, ref, computed, watch } from "vue";
	import { initFlowbite } from "flowbite";
	import {
		getUserListings,
		getActiveCategories,
		getActiveTags,
		deleteListing,
	} from "@/utils/axios";
	import noImage from "@/assets/no-image.png";

	const listings = ref([]);
	const totalListings = ref(0);
	const tagsList = ref([]);
	const pageSize = 10;
	const numberOfElements = ref(0);
	const totalPages = ref(0);
	const firstElementOnPage = computed(() => {
		return (currentPage.value - 1) * pageSize + 1;
	});
	const lastElementOnPage = computed(() => {
		return firstElementOnPage.value + numberOfElements.value - 1;
	});
	const categories = ref([]);
	// filter
	const search = ref("");
	const selectedCategorie = ref("");
	const selectedLocation = ref("");
	const currentPage = ref(1);
	const minPrice = ref("");
	const maxPrice = ref("");
	const checkedStatus = ref([]);
	const checkedTags = ref([]);

	const filtersOn = computed(
		() =>
			selectedCategorie.value ||
			selectedLocation.value ||
			minPrice.value ||
			maxPrice.value ||
			checkedStatus.value.length > 0 ||
			checkedTags.value.length > 0
	);

	const resetFilters = () => {
		selectedCategorie.value = "";
		selectedLocation.value = "";
		minPrice.value = "";
		maxPrice.value = "";
		checkedStatus.value = [];
		checkedTags.value = [];
		handleSearch();
	};

	// menu dropdown filter

	const filterTab = ref(1);
	const statusList = ref([
		{ id: 1, name: "Available" },
		{ id: 2, name: "Out of Stock" },
		{ id: 3, name: "Paused" },
		{ id: 5, name: "Draft" },
		{ id: 6, name: "Blocked" },
		{ id: 7, name: "Unpublished" },
	]);

	const deleteId = ref("");

	onBeforeMount(async () => {
		await getPageData();
		initFlowbite();
	});

	watch(currentPage, async () => {
		await getPageData();
	});

	const getPageData = async () => {
		const params = {
			category: selectedCategorie.value,
			name: search.value,
			location: selectedLocation.value,
			pricemin: minPrice.value,
			pricemax: maxPrice.value,
			page: currentPage.value,
			tags: checkedTags.value,
			status: checkedStatus.value,
		};
		const { data } = await getUserListings(params);
		if (data) {
			listings.value = data.content;
			totalListings.value = data.totalElements;
			totalPages.value = data.totalPages;
			numberOfElements.value = data.numberOfElements;
		}
		const categoriesResponse = await getActiveCategories();
		if (categoriesResponse) categories.value = categoriesResponse.data;

		const tagsResponse = await getActiveTags();
		if (tagsResponse) tagsList.value = tagsResponse.data;
	};

	const priceFormat = price => {
		return "$ " + price.toLocaleString("es-CO");
	};

	const handleSearch = async () => {
		currentPage.value = 1;
		const params = {
			category: selectedCategorie.value,
			name: search.value,
			location: selectedLocation.value,
			pricemin: minPrice.value,
			pricemax: maxPrice.value,
			page: currentPage.value,
			tags: checkedTags.value,
			status: checkedStatus.value,
		};
		const { data } = await getUserListings(params);
		if (data) {
			listings.value = data.content;
			totalListings.value = data.totalElements;
			totalPages.value = data.totalPages;
			numberOfElements.value = data.numberOfElements;
		}
	};

	const handleDelete = async id => {
		deleteId.value = id;
	};

	const confirmDelete = async () => {
		await deleteListing(deleteId.value);
		await getPageData();
	};
</script>

<template>
	<div class="w-full">
		<div class="sticky top-0 mt-6 w-full bg-white py-2 text-center">
			<h1>Publications</h1>
		</div>
		<section class="p-3 sm:p-5">
			<div class="mx-auto max-w-screen-xl px-4 lg:px-12">
				<div
					class="relative bg-white shadow-md dark:bg-gray-800 sm:rounded-lg"
				>
					<!-- header -->
					<div
						class="flex flex-col items-center justify-between space-y-3 p-4 md:flex-row md:space-x-4 md:space-y-0"
					>
						<!-- search bar -->
						<div class="w-1/2">
							<div class="flex items-center">
								<label
									for="simple-search"
									class="sr-only"
									>Search</label
								>
								<div class="relative w-full">
									<div
										class="pointer-events-none absolute inset-y-0 left-0 flex items-center pl-3"
									>
										<svg
											aria-hidden="true"
											class="h-5 w-5 text-gray-500 dark:text-gray-400"
											fill="currentColor"
											viewbox="0 0 20 20"
											xmlns="http://www.w3.org/2000/svg"
										>
											<path
												fill-rule="evenodd"
												d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z"
												clip-rule="evenodd"
											/>
										</svg>
									</div>
									<input
										type="text"
										id="simple-search"
										v-model="search"
										@keypress.enter="handleSearch"
										class="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2 pl-10 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
										placeholder="Search"
									/>
								</div>
							</div>
						</div>
						<!-- search button -->
						<button
							type="submit"
							@click="handleSearch"
							class="ms-2 inline-flex items-center rounded-lg border border-blue-700 bg-blue-700 px-3 py-2.5 text-sm font-medium text-white hover:bg-blue-800 focus:outline-none focus:ring-4 focus:ring-blue-300 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800"
						>
							<svg
								class="me-2 h-4 w-4"
								aria-hidden="true"
								xmlns="http://www.w3.org/2000/svg"
								fill="none"
								viewBox="0 0 20 20"
							>
								<path
									stroke="currentColor"
									stroke-linecap="round"
									stroke-linejoin="round"
									stroke-width="2"
									d="m19 19-4-4m0-7A7 7 0 1 1 1 8a7 7 0 0 1 14 0Z"
								/></svg
							>Search
						</button>
						<!-- category  -->
						<div class="w-1/3">
							<select
								id="categories"
								v-model="selectedCategorie"
								class="block w-full rounded-lg border border-gray-300 bg-gray-50 px-4 py-2 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500"
							>
								<option
									selected
									value=""
								>
									Categories
								</option>
								<option
									v-for="category in categories"
									:value="category.id"
									:key="category.id"
								>
									{{ category.name }}
								</option>
							</select>
						</div>
						<!-- location -->
						<div class="w-1/3">
							<select
								id="locations"
								v-model="selectedLocation"
								class="block w-full rounded-lg border border-gray-300 bg-gray-50 px-4 py-2 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500"
							>
								<option
									selected
									value=""
								>
									Locations
								</option>
								<option>Bucaramanga</option>
								<option>Bogota</option>
								<option>Cali</option>
								<option>Medellin</option>
							</select>
						</div>
						<div
							class="flex w-full flex-shrink-0 flex-col items-stretch justify-end space-y-2 md:w-auto md:flex-row md:items-center md:space-x-3 md:space-y-0"
						>
							<div
								class="flex w-full items-center space-x-3 md:w-auto"
							>
								<button
									id="filterDropdownButton"
									data-dropdown-toggle="filterDropdown"
									class="flex w-full items-center justify-center rounded-lg border border-gray-200 bg-white px-4 py-2 text-sm font-medium text-gray-900 hover:bg-gray-100 hover:text-blue-500 focus:z-10 focus:outline-none focus:ring-4 focus:ring-gray-200 dark:border-gray-600 dark:bg-gray-800 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white dark:focus:ring-gray-700 md:w-auto"
									type="button"
								>
									<FunnelIcon
										class="mr-2 h-4 w-4 text-gray-400"
									/>
									Filters
									<ChevronDownIcon
										class="-mr-1 ml-1.5 h-5 w-5"
									/>
								</button>
								<div
									id="filterDropdown"
									class="z-10 hidden w-48 rounded-lg bg-white p-3 shadow dark:bg-gray-700"
								>
									<div
										class="mb-4 border-b text-sm font-medium text-gray-900 dark:text-white"
									>
										<ul
											class="-mb-px flex flex-wrap gap-2 text-center text-sm font-medium"
										>
											<li>
												<button
													type="button"
													@click="filterTab = 1"
													class="pb-2 text-gray-500 hover:text-gray-900"
													:class="{
														'text-blue-500':
															filterTab === 1,
													}"
												>
													Tags
												</button>
											</li>
											<li>
												<button
													type="button"
													@click="filterTab = 2"
													class="pb-2 text-gray-500 hover:text-gray-900"
													:class="{
														'text-blue-500':
															filterTab === 2,
													}"
												>
													Price
												</button>
											</li>
											<li>
												<button
													type="button"
													@click="filterTab = 3"
													class="pb-2 text-gray-500 hover:text-gray-900"
													:class="{
														'text-blue-500':
															filterTab === 3,
													}"
												>
													Status
												</button>
											</li>
										</ul>
									</div>
									<ul
										v-if="filterTab === 1"
										class="space-y-2 text-sm"
										aria-labelledby="filterDropdownButton"
									>
										<li
											v-for="tag in tagsList"
											:key="tag"
											class="flex items-center"
										>
											<input
												:id="tag"
												type="checkbox"
												:value="tag"
												v-model="checkedTags"
												class="h-4 w-4 rounded border-gray-300 bg-gray-100 text-blue-600 focus:ring-2 focus:ring-blue-500"
											/>
											<label
												:for="tag"
												class="ml-2 text-sm font-medium text-gray-900 dark:text-gray-100"
												>{{ tag }}</label
											>
										</li>
									</ul>
									<div
										v-if="filterTab === 2"
										class="grid grid-cols-2 gap-4 text-sm"
									>
										<div class="w-full">
											<label for="fromPrice">From</label>
											<input
												type="number"
												id="fromPrice"
												v-model="minPrice"
												min="1"
												max="1000000000"
												class="mb-2 block w-full rounded-lg border border-gray-300 p-2 text-xs"
											/>
										</div>
										<div class="w-full">
											<label for="toPrice">To</label>
											<input
												type="number"
												id="toPrice"
												v-model="maxPrice"
												min="1"
												max="1000000000"
												class="mb-2 block w-full rounded-lg border border-gray-300 p-2 text-xs"
											/>
										</div>
									</div>
									<ul
										v-if="filterTab === 3"
										class="space-y-2 text-sm"
										aria-labelledby="filterDropdownButton"
									>
										<li
											v-for="status in statusList"
											:key="status.id"
											class="flex items-center"
										>
											<input
												:id="status.name"
												type="checkbox"
												:value="status.id"
												v-model="checkedStatus"
												class="h-4 w-4 rounded border-gray-300 bg-gray-100 text-blue-600 focus:ring-2 focus:ring-blue-500"
											/>
											<label
												:for="status.name"
												class="ml-2 text-sm font-medium text-gray-900 dark:text-gray-100"
												>{{ status.name }}</label
											>
										</li>
									</ul>
								</div>
							</div>
						</div>
					</div>
					<!-- remove filter button -->
					<button
						v-if="filtersOn"
						type="button"
						@click="resetFilters"
						class="relative -mt-2 mb-1 ml-4 block w-max text-sm text-red-500 underline hover:no-underline"
					>
						Reset Filters
					</button>
					<!-- entries -->
					<div class="overflow-x-auto">
						<table
							class="w-full text-left text-sm text-gray-500 dark:text-gray-400"
						>
							<thead
								class="bg-gray-50 text-xs uppercase text-gray-700 dark:bg-gray-700 dark:text-gray-400"
							>
								<tr>
									<th
										scope="col"
										class="px-4 py-3"
									>
										Product name
									</th>
									<th
										scope="col"
										class="px-4 py-3"
									>
										Status
									</th>
									<th
										scope="col"
										class="px-4 py-3"
									>
										Location
									</th>
									<th
										scope="col"
										class="px-4 py-3"
									>
										Stock
									</th>
									<th
										scope="col"
										class="px-4 py-3"
									>
										Price
									</th>
									<th
										scope="col"
										class="px-4 py-3"
									>
										<span class="sr-only">Actions</span>
									</th>
								</tr>
							</thead>
							<tbody>
								<!-- Entry -->
								<tr
									v-for="listing in listings"
									:key="listing.id"
									class="border-b dark:border-gray-700"
								>
									<th
										scope="row"
										class="max-w-sm truncate whitespace-nowrap px-4 py-3 font-medium text-gray-900 dark:text-white"
									>
										<img
											:src="listing.thumbnail || noImage"
											:alt="listing.name"
											class="mr-2 inline-block h-10 w-10 rounded-md object-cover"
										/>
										{{ listing.name }}
									</th>
									<td class="whitespace-nowrap px-4 py-3">
										{{ listing.status }}
									</td>
									<td class="whitespace-nowrap px-4 py-3">
										{{ listing.location }}
									</td>
									<td class="whitespace-nowrap px-4 py-3">
										{{ listing.stock }}
									</td>
									<td class="whitespace-nowrap px-4 py-3">
										{{ priceFormat(listing.price) }}
									</td>
									<td
										class="flex items-center justify-end px-4 py-3"
									>
										<button
											:id="
												'entry' +
												listing.id +
												'-dropdown-button'
											"
											:data-dropdown-toggle="
												'entry' +
												listing.id +
												'-dropdown'
											"
											class="inline-flex items-center rounded-lg p-0.5 text-center text-sm font-medium text-gray-500 hover:text-gray-800 focus:outline-none dark:text-gray-400 dark:hover:text-gray-100"
											type="button"
										>
											<svg
												class="h-5 w-5"
												aria-hidden="true"
												fill="currentColor"
												viewbox="0 0 20 20"
												xmlns="http://www.w3.org/2000/svg"
											>
												<path
													d="M6 10a2 2 0 11-4 0 2 2 0 014 0zM12 10a2 2 0 11-4 0 2 2 0 014 0zM16 12a2 2 0 100-4 2 2 0 000 4z"
												/>
											</svg>
										</button>
										<div
											:id="
												'entry' +
												listing.id +
												'-dropdown'
											"
											class="z-10 hidden w-44 divide-y divide-gray-100 rounded bg-white shadow dark:divide-gray-600 dark:bg-gray-700"
										>
											<ul
												class="py-1 text-sm text-gray-700 dark:text-gray-200"
												:aria-labelledby="
													'entry' +
													listing.id +
													'-dropdown-button'
												"
											>
												<li
													v-if="
														listing.status !==
														'Draft'
													"
												>
													<router-link
														:to="
															'/listings/' +
															listing.id
														"
														class="block px-4 py-2 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white"
														>Show</router-link
													>
												</li>
												<li
													v-if="
														listing.status ===
														'Draft'
													"
												>
													<router-link
														:to="
															'/listings/new?draft=' +
															listing.id
														"
														class="block px-4 py-2 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white"
														>Edit</router-link
													>
												</li>
											</ul>
											<div class="py-1">
												<button
													type="button"
													@click="
														handleDelete(listing.id)
													"
													data-modal-target="deleteModal"
													data-modal-toggle="deleteModal"
													class="block w-full px-4 py-2 text-left text-sm text-gray-700 hover:bg-gray-100 dark:text-gray-200 dark:hover:bg-gray-600 dark:hover:text-white"
												>
													Delete
												</button>
											</div>
										</div>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					<!-- pagination -->
					<nav
						class="flex flex-col items-start justify-between space-y-3 p-4 md:flex-row md:items-center md:space-y-0"
						aria-label="Table navigation"
					>
						<span
							class="text-sm font-normal text-gray-500 dark:text-gray-400"
						>
							Showing
							<span
								v-if="totalListings > 0"
								class="font-semibold text-gray-900 dark:text-white"
								>{{ firstElementOnPage }}-{{
									lastElementOnPage
								}}</span
							>
							<span
								v-else
								class="font-semibold text-gray-900 dark:text-white"
							>
								0
							</span>
							of
							<span
								class="font-semibold text-gray-900 dark:text-white"
								>{{ totalListings }}</span
							>
						</span>
						<div class="flex">
							<!-- Previous Button -->
							<button
								type="button"
								:disabled="currentPage === 1"
								@click="currentPage--"
								class="me-3 flex h-8 items-center justify-center rounded-lg border border-gray-300 bg-white px-3 text-sm font-medium text-gray-500 hover:bg-gray-100 hover:text-gray-700 disabled:cursor-not-allowed disabled:opacity-50 dark:border-gray-700 dark:bg-gray-800 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white"
							>
								<svg
									class="me-2 h-3.5 w-3.5 rtl:rotate-180"
									aria-hidden="true"
									xmlns="http://www.w3.org/2000/svg"
									fill="none"
									viewBox="0 0 14 10"
								>
									<path
										stroke="currentColor"
										stroke-linecap="round"
										stroke-linejoin="round"
										stroke-width="2"
										d="M13 5H1m0 0 4 4M1 5l4-4"
									/>
								</svg>
								Previous
							</button>
							<button
								type="button"
								:disabled="currentPage === totalPages"
								@click="currentPage++"
								class="flex h-8 items-center justify-center rounded-lg border border-gray-300 bg-white px-3 text-sm font-medium text-gray-500 hover:bg-gray-100 hover:text-gray-700 disabled:cursor-not-allowed disabled:opacity-50 dark:border-gray-700 dark:bg-gray-800 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white"
							>
								Next
								<svg
									class="ms-2 h-3.5 w-3.5 rtl:rotate-180"
									aria-hidden="true"
									xmlns="http://www.w3.org/2000/svg"
									fill="none"
									viewBox="0 0 14 10"
								>
									<path
										stroke="currentColor"
										stroke-linecap="round"
										stroke-linejoin="round"
										stroke-width="2"
										d="M1 5h12m0 0L9 1m4 4L9 9"
									/>
								</svg>
							</button>
						</div>
					</nav>
				</div>
			</div>
		</section>
		<!-- confirm delete -->
		<div
			id="deleteModal"
			tabindex="-1"
			aria-hidden="true"
			class="fixed left-0 right-0 top-0 z-50 hidden h-modal w-full items-center justify-center overflow-y-auto overflow-x-hidden md:inset-0 md:h-full"
		>
			<div class="relative h-full w-full max-w-md p-4 md:h-auto">
				<!-- Modal content -->
				<div
					class="relative rounded-lg bg-white p-4 text-center shadow dark:bg-gray-800 sm:p-5"
				>
					<button
						type="button"
						class="absolute right-2.5 top-2.5 ml-auto inline-flex items-center rounded-lg bg-transparent p-1.5 text-sm text-gray-400 hover:bg-gray-200 hover:text-gray-900 dark:hover:bg-gray-600 dark:hover:text-white"
						data-modal-toggle="deleteModal"
					>
						<svg
							aria-hidden="true"
							class="h-5 w-5"
							fill="currentColor"
							viewBox="0 0 20 20"
							xmlns="http://www.w3.org/2000/svg"
						>
							<path
								fill-rule="evenodd"
								d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
								clip-rule="evenodd"
							></path>
						</svg>
						<span class="sr-only">Close modal</span>
					</button>
					<svg
						class="mx-auto mb-3.5 h-11 w-11 text-gray-400 dark:text-gray-500"
						aria-hidden="true"
						fill="currentColor"
						viewBox="0 0 20 20"
						xmlns="http://www.w3.org/2000/svg"
					>
						<path
							fill-rule="evenodd"
							d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z"
							clip-rule="evenodd"
						></path>
					</svg>
					<p class="mb-4 text-gray-500 dark:text-gray-300">
						Are you sure you want to delete this item?
					</p>
					<div class="flex items-center justify-center space-x-4">
						<button
							type="button"
							data-modal-toggle="deleteModal"
							@click="confirmDelete"
							class="rounded-lg bg-endava-500 px-3 py-2 text-center text-sm font-medium text-white hover:bg-endava-600 focus:outline-none focus:ring-4 focus:ring-endava-300 active:bg-endava-700 dark:bg-endava-500 dark:hover:bg-endava-500 dark:focus:ring-endava-900"
						>
							Yes
						</button>
						<button
							data-modal-toggle="deleteModal"
							type="button"
							class="focus:ring-primary-300 rounded-lg border border-gray-200 bg-white px-3 py-2 text-sm font-medium text-gray-500 hover:bg-gray-100 hover:text-gray-900 focus:z-10 focus:outline-none focus:ring-4 dark:border-gray-500 dark:bg-gray-700 dark:text-gray-300 dark:hover:bg-gray-600 dark:hover:text-white dark:focus:ring-gray-600"
						>
							No
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>
