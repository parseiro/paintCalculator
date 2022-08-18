import { IParede, NewParede } from './parede.model';

export const sampleWithRequiredData: IParede = {
  id: 54297,
  largura: 84,
  altura: 43,
};

export const sampleWithPartialData: IParede = {
  id: 64293,
  largura: 7,
  altura: 96,
  numPortas: 11,
};

export const sampleWithFullData: IParede = {
  id: 44424,
  largura: 86,
  altura: 46,
  numPortas: 20,
  numJanelas: 85,
};

export const sampleWithNewData: NewParede = {
  largura: 78,
  altura: 74,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
