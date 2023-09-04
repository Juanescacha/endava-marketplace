<template>
	<header
		class="fixed top-0 z-50 flex w-full flex-wrap border-b bg-white/70 py-2.5 text-sm backdrop-blur-md sm:flex-nowrap sm:justify-start sm:py-4"
	>
		<nav
			class="mx-auto flex w-full max-w-7xl basis-full items-center px-4 sm:px-6 lg:px-8"
			aria-label="Global"
		>
			<div class="mr-5 md:mr-8">
				<a
					class="flex-none text-xl font-semibold"
					href="#"
					aria-label="Brand"
					><img
						src="../assets/endava-logo.png"
						alt="logo"
						class="w-28"
				/></a>
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
							name="icon"
							class="block w-96 rounded-md border-gray-200 bg-white/70 px-4 py-2 pl-11 text-sm shadow-sm focus:z-10 focus:border-endava-400 focus:ring-endava-400"
							placeholder="Search"
						/>
					</div>
				</div>
				<div class="">
					<select
						class="hidden w-52 rounded-md border-gray-200 bg-white/20 py-2 pl-6 pr-11 text-sm shadow-sm focus:z-10 focus:border-gray-200 focus:ring-gray-200 sm:block"
					>
						<option selected>Categories</option>
						<option>Clothes</option>
						<option>Technoloy</option>
						<option>Vehicles</option>
					</select>
				</div>
				<div class="flex gap-4">
					<!-- <button
						type="button"
						class="inline-flex items-center justify-center gap-2 rounded-md border bg-endava-400 px-4 py-3 text-sm font-semibold text-white shadow-sm transition-all hover:bg-endava-500 active:bg-endava-600"
					>
						Buy
						<ShoppingBagIcon class="h-4 w-4 text-current" />
					</button> -->
					<button
						type="button"
						class="inline-flex items-center justify-center gap-2 rounded-md border bg-endava-400 px-4 py-3 text-sm font-semibold text-white shadow-sm transition-all hover:bg-endava-500 active:bg-endava-600"
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

<script setup>
import {
	BellIcon,
	MagnifyingGlassIcon,
	BookOpenIcon,
	ListBulletIcon,
	Cog8ToothIcon,
} from "@heroicons/vue/24/outline";

import { CreditCardIcon } from "@heroicons/vue/24/solid";
</script>
