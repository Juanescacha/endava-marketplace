<script setup>
	import { RouterLink } from "vue-router";
	import { onBeforeMount, ref, computed, watch } from "vue";
	import { initFlowbite } from "flowbite";
	import { getMySales } from "@/utils/axios";
	import noImage from "@/assets/no-image.png";

	const listings = ref([]);
	const totalListings = ref(0);
	const pageSize = 10;
	const numberOfElements = ref(0);
	const totalPages = ref(0);
	const firstElementOnPage = computed(() => {
		return (currentPage.value - 1) * pageSize + 1;
	});
	const lastElementOnPage = computed(() => {
		return firstElementOnPage.value + numberOfElements.value - 1;
	});
	// filter
	const search = ref("");
	const currentPage = ref(1);
	const selectedStatus = ref("");

	const filtersOn = computed(() => selectedStatus.value);

	const resetFilters = () => {
		selectedStatus.value = "";
		handleSearch();
	};

	// status

	const statusList = ref([
		{ id: 1, name: "Pending" },
		{ id: 2, name: "Fulfilled" },
		{ id: 3, name: "Cancelled" },
	]);

	onBeforeMount(async () => {
		await getPageData();
		initFlowbite();
	});

	watch(currentPage, async () => {
		await getPageData();
	});

	const getPageData = async () => {
		const params = {
			type: "purchases",
			name: search.value,
			page: currentPage.value,
			status: selectedStatus.value,
		};
		const { data } = await getMySales(params);
		if (data) {
			listings.value = data.content;
			totalListings.value = data.totalElements;
			totalPages.value = data.totalPages;
			numberOfElements.value = data.numberOfElements;
		}
	};

	const priceFormat = price => {
		return "$ " + price.toLocaleString("es-CO");
	};

	const handleSearch = async () => {
		currentPage.value = 1;
		const params = {
			type: "purchases",
			name: search.value,
			page: currentPage.value,
			status: selectedStatus.value,
		};
		const { data } = await getMySales(params);
		if (data) {
			listings.value = data.content;
			totalListings.value = data.totalElements;
			totalPages.value = data.totalPages;
			numberOfElements.value = data.numberOfElements;
		}
	};
</script>

<template>
	<div class="w-full">
		<div class="sticky top-0 mt-6 w-full bg-white py-2 text-center">
			<h1>Purchases</h1>
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
						<div class="w-full">
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
						<!-- right -->
						<div
							class="flex w-full flex-shrink-0 flex-col items-stretch justify-end space-y-2 md:w-auto md:flex-row md:items-center md:space-x-3 md:space-y-0"
						>
							<!-- status  -->
							<div class="w-full">
								<select
									id="status"
									v-model="selectedStatus"
									class="block w-full rounded-lg border border-gray-300 bg-gray-50 px-4 py-2 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500"
								>
									<option
										selected
										value=""
									>
										Status
									</option>
									<option
										v-for="status in statusList"
										:value="status.id"
										:key="status.id"
									>
										{{ status.name }}
									</option>
								</select>
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
										Quantity
									</th>
									<th
										scope="col"
										class="px-4 py-3"
									>
										Date
									</th>
									<th
										scope="col"
										class="px-4 py-3"
									>
										Seller
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
											:src="
												listing.listing_thumbnail ||
												noImage
											"
											:alt="listing.listing_name"
											class="mr-2 inline-block h-10 w-10 rounded-md object-cover"
										/>
										{{ listing.listing_name }}
									</th>
									<td class="px-4 py-3 whitespace-nowrap">
										{{ listing.status }}
									</td>
									<td class="px-4 py-3 whitespace-nowrap">
										{{ listing.quantity }}
									</td>
									<td class="px-4 py-3 whitespace-nowrap">
										{{ listing.date }}
									</td>
									<td class="px-4 py-3 whitespace-nowrap">
										{{ listing.seller_name }}
									</td>
									<td class="px-4 py-3 whitespace-nowrap">
										{{ priceFormat(listing.listing_price) }}
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
															'/users/me/purchase/' +
															listing.id
														"
														class="block px-4 py-2 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white"
														>Show</router-link
													>
												</li>
											</ul>
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
	</div>
</template>
