# Project Title

Simple Pixabay Image Loader with RecyclerView

## Getting Started

These instructions will help you to setup the library.

### Installing

Step 1. Add the JitPack repository to your build file

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

Step 2. Add the dependency

```
dependencies {
	        implementation 'com.github.Rockerdx:SimplePixabay:Tag'
	}
```
Step 3. Add this code to start the activity

Java
```
        Intent i = new Intent(this,PixaBayActivity.class);
        i.putExtra(PixaBayActivity.INTENT.key,"your api key");
        i.putExtra(PixaBayActivity.INTENT.query,"android"); // this parameter will be used as query on pixabay
        i.putExtra(PixaBayActivity.INTENT.collumn,1); // no of column displayed is the list
        startActivity(i);
```
Kotlin
```
        val intent = Intent(this,PixaBayActivity::class.java)
        intent.putExtra(PixaBayActivity.INTENT.key,"your api key")
        intent.putExtra(PixaBayActivity.INTENT.query,"flower") // this parameter will be used as query on pixabay
        intent.putExtra(PixaBayActivity.INTENT.collumn,3) // no of column displayed is the list
        startActivity(intent)
```

## Authors

* **M. Rizky Putra**
