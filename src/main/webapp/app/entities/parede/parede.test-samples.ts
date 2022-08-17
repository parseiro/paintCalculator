import { IParede, NewParede } from './parede.model';

export const sampleWithRequiredData: IParede = {
  id: 54297,
  largura: 83938,
  altura: 42575,
};

export const sampleWithPartialData: IParede = {
  id: 64293,
  largura: 7644,
  altura: 95180,
  numPortas: 52759,
};

export const sampleWithFullData: IParede = {
  id: 44424,
  largura: 85905,
  altura: 45705,
  numPortas: 97008,
  numJanelas: 85030,
};

export const sampleWithNewData: NewParede = {
  largura: 77314,
  altura: 74063,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
