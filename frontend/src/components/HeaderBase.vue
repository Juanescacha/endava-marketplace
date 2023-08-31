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
						<svg
							class="h-3.5 w-3.5"
							xmlns="http://www.w3.org/2000/svg"
							width="16"
							height="16"
							fill="currentColor"
							viewBox="0 0 16 16"
						>
							<path
								d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001c.03.04.062.078.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1.007 1.007 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0z"
							/>
						</svg>
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
							<svg
								class="h-4 w-4 text-gray-400"
								xmlns="http://www.w3.org/2000/svg"
								width="16"
								height="16"
								fill="currentColor"
								viewBox="0 0 16 16"
							>
								<path
									d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001c.03.04.062.078.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1.007 1.007 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0z"
								/>
							</svg>
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

				<!-- categorie list -->

				<div class="">
					<select
						class="hidden w-52 rounded-md border-gray-200 bg-white/0 py-2 pl-6 pr-11 text-sm shadow-sm focus:z-10 focus:border-gray-200 focus:ring-gray-200 sm:block"
					>
						<option selected>Categories</option>
						<option>Clothes</option>
						<option>Technoloy</option>
						<option>Vehicles</option>
					</select>
				</div>

				<!-- buy or sell buttons -->
				<div class="flex gap-5">
					<button
						type="button"
						class="inline-flex items-center justify-center gap-2 rounded-md border bg-endava-400 px-4 py-3 text-sm font-semibold text-white shadow-sm transition-all hover:bg-endava-500 active:bg-endava-600"
					>
						Buy
						<svg
							class="h-3.5 w-3.5"
							xmlns="http://www.w3.org/2000/svg"
							width="16"
							height="16"
							fill="currentColor"
							viewBox="0 0 16 16"
						>
							<path
								d="M5.071 1.243a.5.5 0 0 1 .858.514L3.383 6h9.234L10.07 1.757a.5.5 0 1 1 .858-.514L13.783 6H15.5a.5.5 0 0 1 .5.5v2a.5.5 0 0 1-.5.5H15v5a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V9H.5a.5.5 0 0 1-.5-.5v-2A.5.5 0 0 1 .5 6h1.717L5.07 1.243zM3.5 10.5a.5.5 0 1 0-1 0v3a.5.5 0 0 0 1 0v-3zm2.5 0a.5.5 0 1 0-1 0v3a.5.5 0 0 0 1 0v-3zm2.5 0a.5.5 0 1 0-1 0v3a.5.5 0 0 0 1 0v-3zm2.5 0a.5.5 0 1 0-1 0v3a.5.5 0 0 0 1 0v-3zm2.5 0a.5.5 0 1 0-1 0v3a.5.5 0 0 0 1 0v-3z"
							/>
						</svg>
					</button>
					<button
						type="button"
						class="inline-flex items-center justify-center gap-2 rounded-md border bg-white/50 px-4 py-3 align-middle text-sm font-medium text-gray-700 shadow-sm transition-all hover:bg-gray-100/50 active:bg-gray-200"
					>
						Sell
						<svg
							class="h-auto w-2.5"
							width="17"
							height="16"
							viewBox="0 0 17 16"
							fill="none"
							xmlns="http://www.w3.org/2000/svg"
						>
							<path
								fill-rule="evenodd"
								clip-rule="evenodd"
								d="M1 7C0.447715 7 -3.73832e-07 7.44771 -3.49691e-07 8C-3.2555e-07 8.55228 0.447715 9 1 9L13.0858 9L7.79289 14.2929C7.40237 14.6834 7.40237 15.3166 7.79289 15.7071C8.18342 16.0976 8.81658 16.0976 9.20711 15.7071L16.0303 8.88388C16.5185 8.39573 16.5185 7.60427 16.0303 7.11612L9.20711 0.292893C8.81658 -0.0976318 8.18342 -0.0976318 7.79289 0.292893C7.40237 0.683417 7.40237 1.31658 7.79289 1.70711L13.0858 7L1 7Z"
								fill="currentColor"
							/>
						</svg>
					</button>
				</div>

				<div class="flex flex-row items-center justify-end gap-2">
					<button
						type="button"
						class="inline-flex h-[2.375rem] w-[2.375rem] flex-shrink-0 items-center justify-center gap-2 rounded-full bg-white/5 align-middle text-xs font-medium text-gray-700 transition-all hover:bg-gray-100/50 hover:shadow-md active:bg-gray-200/50"
					>
						<svg
							class="h-3.5 w-3.5"
							xmlns="http://www.w3.org/2000/svg"
							width="16"
							height="16"
							fill="currentColor"
							viewBox="0 0 16 16"
						>
							<path
								d="M8 16a2 2 0 0 0 2-2H6a2 2 0 0 0 2 2zM8 1.918l-.797.161A4.002 4.002 0 0 0 4 6c0 .628-.134 2.197-.459 3.742-.16.767-.376 1.566-.663 2.258h10.244c-.287-.692-.502-1.49-.663-2.258C12.134 8.197 12 6.628 12 6a4.002 4.002 0 0 0-3.203-3.92L8 1.917zM14.22 12c.223.447.481.801.78 1H1c.299-.199.557-.553.78-1C2.68 10.2 3 6.88 3 6c0-2.42 1.72-4.44 4.005-4.901a1 1 0 1 1 1.99 0A5.002 5.002 0 0 1 13 6c0 .88.32 4.2 1.22 6z"
							/>
						</svg>
					</button>
					<button
						type="button"
						class="inline-flex h-[2.375rem] w-[2.375rem] flex-shrink-0 items-center justify-center gap-2 rounded-full bg-white/5 align-middle text-xs font-medium text-gray-700 transition-all hover:bg-gray-100/50 hover:shadow-md active:bg-gray-200/50"
						data-hs-offcanvas="#hs-offcanvas-right"
					>
						<svg
							class="h-3.5 w-3.5"
							xmlns="http://www.w3.org/2000/svg"
							width="16"
							height="16"
							fill="currentColor"
							viewBox="0 0 16 16"
						>
							<path
								d="M5.5 2A3.5 3.5 0 0 0 2 5.5v5A3.5 3.5 0 0 0 5.5 14h5a3.5 3.5 0 0 0 3.5-3.5V8a.5.5 0 0 1 1 0v2.5a4.5 4.5 0 0 1-4.5 4.5h-5A4.5 4.5 0 0 1 1 10.5v-5A4.5 4.5 0 0 1 5.5 1H8a.5.5 0 0 1 0 1H5.5z"
							/>
							<path d="M16 3a3 3 0 1 1-6 0 3 3 0 0 1 6 0z" />
						</svg>
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
									class="flex items-center gap-x-3.5 rounded-md px-3 py-2 text-sm text-gray-800 hover:bg-gray-100 focus:ring-2 focus:ring-endava-400"
									href="#"
								>
									<svg
										class="flex-none"
										width="16"
										height="16"
										viewBox="0 0 16 16"
										fill="currentColor"
									>
										<path
											d="M8 16a2 2 0 0 0 2-2H6a2 2 0 0 0 2 2zM8 1.918l-.797.161A4.002 4.002 0 0 0 4 6c0 .628-.134 2.197-.459 3.742-.16.767-.376 1.566-.663 2.258h10.244c-.287-.692-.502-1.49-.663-2.258C12.134 8.197 12 6.628 12 6a4.002 4.002 0 0 0-3.203-3.92L8 1.917zM14.22 12c.223.447.481.801.78 1H1c.299-.199.557-.553.78-1C2.68 10.2 3 6.88 3 6c0-2.42 1.72-4.44 4.005-4.901a1 1 0 1 1 1.99 0A5.002 5.002 0 0 1 13 6c0 .88.32 4.2 1.22 6z"
										/>
									</svg>
									Notifications
								</a>
								<a
									class="flex items-center gap-x-3.5 rounded-md px-3 py-2 text-sm text-gray-800 hover:bg-gray-100 focus:ring-2 focus:ring-endava-400"
									href="#"
								>
									<svg
										class="flex-none"
										width="16"
										height="16"
										viewBox="0 0 16 16"
										fill="currentColor"
									>
										<path
											d="M1.92.506a.5.5 0 0 1 .434.14L3 1.293l.646-.647a.5.5 0 0 1 .708 0L5 1.293l.646-.647a.5.5 0 0 1 .708 0L7 1.293l.646-.647a.5.5 0 0 1 .708 0L9 1.293l.646-.647a.5.5 0 0 1 .708 0l.646.647.646-.647a.5.5 0 0 1 .708 0l.646.647.646-.647a.5.5 0 0 1 .801.13l.5 1A.5.5 0 0 1 15 2v12a.5.5 0 0 1-.053.224l-.5 1a.5.5 0 0 1-.8.13L13 14.707l-.646.647a.5.5 0 0 1-.708 0L11 14.707l-.646.647a.5.5 0 0 1-.708 0L9 14.707l-.646.647a.5.5 0 0 1-.708 0L7 14.707l-.646.647a.5.5 0 0 1-.708 0L5 14.707l-.646.647a.5.5 0 0 1-.708 0L3 14.707l-.646.647a.5.5 0 0 1-.801-.13l-.5-1A.5.5 0 0 1 1 14V2a.5.5 0 0 1 .053-.224l.5-1a.5.5 0 0 1 .367-.27zm.217 1.338L2 2.118v11.764l.137.274.51-.51a.5.5 0 0 1 .707 0l.646.647.646-.646a.5.5 0 0 1 .708 0l.646.646.646-.646a.5.5 0 0 1 .708 0l.646.646.646-.646a.5.5 0 0 1 .708 0l.646.646.646-.646a.5.5 0 0 1 .708 0l.646.646.646-.646a.5.5 0 0 1 .708 0l.509.509.137-.274V2.118l-.137-.274-.51.51a.5.5 0 0 1-.707 0L12 1.707l-.646.647a.5.5 0 0 1-.708 0L10 1.707l-.646.647a.5.5 0 0 1-.708 0L8 1.707l-.646.647a.5.5 0 0 1-.708 0L6 1.707l-.646.647a.5.5 0 0 1-.708 0L4 1.707l-.646.647a.5.5 0 0 1-.708 0l-.509-.51z"
										/>
										<path
											d="M3 4.5a.5.5 0 0 1 .5-.5h6a.5.5 0 1 1 0 1h-6a.5.5 0 0 1-.5-.5zm0 2a.5.5 0 0 1 .5-.5h6a.5.5 0 1 1 0 1h-6a.5.5 0 0 1-.5-.5zm0 2a.5.5 0 0 1 .5-.5h6a.5.5 0 1 1 0 1h-6a.5.5 0 0 1-.5-.5zm0 2a.5.5 0 0 1 .5-.5h6a.5.5 0 0 1 0 1h-6a.5.5 0 0 1-.5-.5zm8-6a.5.5 0 0 1 .5-.5h1a.5.5 0 0 1 0 1h-1a.5.5 0 0 1-.5-.5zm0 2a.5.5 0 0 1 .5-.5h1a.5.5 0 0 1 0 1h-1a.5.5 0 0 1-.5-.5zm0 2a.5.5 0 0 1 .5-.5h1a.5.5 0 0 1 0 1h-1a.5.5 0 0 1-.5-.5zm0 2a.5.5 0 0 1 .5-.5h1a.5.5 0 0 1 0 1h-1a.5.5 0 0 1-.5-.5z"
										/>
									</svg>
									Publications
								</a>
								<a
									class="flex items-center gap-x-3.5 rounded-md px-3 py-2 text-sm text-gray-800 hover:bg-gray-100 focus:ring-2 focus:ring-endava-400"
									href="#"
								>
									<svg
										class="flex-none"
										width="16"
										height="16"
										viewBox="0 0 16 16"
										fill="currentColor"
									>
										<path
											fill-rule="evenodd"
											d="M7.646 10.854a.5.5 0 0 0 .708 0l2-2a.5.5 0 0 0-.708-.708L8.5 9.293V5.5a.5.5 0 0 0-1 0v3.793L6.354 8.146a.5.5 0 1 0-.708.708l2 2z"
										/>
										<path
											d="M4.406 3.342A5.53 5.53 0 0 1 8 2c2.69 0 4.923 2 5.166 4.579C14.758 6.804 16 8.137 16 9.773 16 11.569 14.502 13 12.687 13H3.781C1.708 13 0 11.366 0 9.318c0-1.763 1.266-3.223 2.942-3.593.143-.863.698-1.723 1.464-2.383zm.653.757c-.757.653-1.153 1.44-1.153 2.056v.448l-.445.049C2.064 6.805 1 7.952 1 9.318 1 10.785 2.23 12 3.781 12h8.906C13.98 12 15 10.988 15 9.773c0-1.216-1.02-2.228-2.313-2.228h-.5v-.5C12.188 4.825 10.328 3 8 3a4.53 4.53 0 0 0-2.941 1.1z"
										/>
									</svg>
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
<script setup></script>
