import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export interface Spec extends TurboModule {
  isImageBlack(path: string): Promise<boolean>;
  isImageBlurry(path: string): Promise<boolean>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('ImageClarity');
