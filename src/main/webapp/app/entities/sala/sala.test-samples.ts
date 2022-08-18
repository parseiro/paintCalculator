import { ISala, NewSala } from './sala.model';

export const sampleWithRequiredData: ISala = {
  id: 72944,
  nome: 'copy',
};

export const sampleWithPartialData: ISala = {
  id: 36331,
  nome: 'Future 24/365',
};

export const sampleWithFullData: ISala = {
  id: 53575,
  nome: 'Realigned XML',
};

export const sampleWithNewData: NewSala = {
  nome: 'connecting Bola',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
