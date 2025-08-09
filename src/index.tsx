import ImageClarity from './NativeImageClarity';

export function isImageBlack(path: string): Promise<boolean> {
  return ImageClarity.isImageBlack(path);
}

export function isImageBlurry(path: string): Promise<boolean> {
  return ImageClarity.isImageBlurry(path);
}
