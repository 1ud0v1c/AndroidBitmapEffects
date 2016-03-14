# AndroidBitmapEffects

![Application view](https://github.com/1ud0v1c/AndroidBitmapEffects/raw/master/screenshots/application.png "")

Just a little library for Android to be able to play with Bitmap 

## Examples 

Here you can find few examples of the effects we can have, first we need to recover the Drawable from our ImageView :

```java
Drawable imageDrawable = bitmapView.getDrawable();
```

- Flip effect :

![Flip effect](https://github.com/1ud0v1c/AndroidBitmapEffects/raw/master/screenshots/flip.png "")

```java
bitmapView.setImageBitmap(BitmapBuilder.flip(BitmapBuilder.drawableToBitmap(imageDrawable)));
```

- Grayscale effect :

![Grayscale effect](https://github.com/1ud0v1c/AndroidBitmapEffects/raw/master/screenshots/grayscale.png "")

The second argument is the desire saturation

```java
bitmapView.setImageBitmap(BitmapBuilder.toGrayscale(BitmapBuilder.drawableToBitmap(imageDrawable), 0.2f));
```

- Invert effect :

![Invert effect](https://github.com/1ud0v1c/AndroidBitmapEffects/raw/master/screenshots/invert.png "")

```java
bitmapView.setImageBitmap(BitmapBuilder.invert(BitmapBuilder.drawableToBitmap(imageDrawable)));
```

- Blur effect : 

![Blur effect](https://github.com/1ud0v1c/AndroidBitmapEffects/raw/master/screenshots/blur.png "")

```java
bitmapView.setImageBitmap(BitmapBuilder.blur(context, BitmapBuilder.drawableToBitmap(imageDrawable)));
```

- Center crop

![Center crop](https://github.com/1ud0v1c/AndroidBitmapEffects/raw/master/screenshots/cropcenter.png "")

```java
bitmapView.setImageBitmap(BitmapBuilder.centerCrop(BitmapBuilder.drawableToBitmap(imageDrawable)));
```

- Sepia effect

![Sepia effect](https://github.com/1ud0v1c/AndroidBitmapEffects/raw/master/screenshots/sepia.png "")

```java
bitmapView.setImageBitmap(BitmapBuilder.toSepia(BitmapBuilder.drawableToBitmap(imageDrawable)));			    
```

- Glow effect 

![Glow effect](https://github.com/1ud0v1c/AndroidBitmapEffects/raw/master/screenshots/glow.png "")

The second argument is the radius of the glow
The third argument is the glow color, here a blue 

```java
bitmapView.setImageBitmap(BitmapBuilder.glowEffect(BitmapBuilder.drawableToBitmap(imageDrawable), 40, 0xFF25BBEF));
```

- Reflection effect

![Reflection effect](https://github.com/1ud0v1c/AndroidBitmapEffects/raw/master/screenshots/reflection.png "")

The second argument is the gap between the original image and the reflected image 

```java
bitmapView.setImageBitmap(BitmapBuilder.reflectionEffect(BitmapBuilder.drawableToBitmap(imageDrawable), 4));
```

- Rounded Corners

![Rounded corners](https://github.com/1ud0v1c/AndroidBitmapEffects/raw/master/screenshots/rounded_corners.png "")

The second argument is the size of the radius. 

```java
bitmapView.setImageBitmap(BitmapBuilder.getRoundedCornerImage(BitmapBuilder.drawableToBitmap(imageDrawable), 50));
```
