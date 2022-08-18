import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IParede, NewParede } from '../parede.model';

export type PartialUpdateParede = Partial<IParede> & Pick<IParede, 'id'>;

export type EntityResponseType = HttpResponse<IParede>;
export type EntityArrayResponseType = HttpResponse<IParede[]>;

@Injectable({ providedIn: 'root' })
export class ParedeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/paredes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(parede: NewParede): Observable<EntityResponseType> {
    return this.http.post<IParede>(this.resourceUrl, parede, { observe: 'response' });
  }

  update(parede: IParede): Observable<EntityResponseType> {
    return this.http.put<IParede>(`${this.resourceUrl}/${this.getParedeIdentifier(parede)}`, parede, { observe: 'response' });
  }

  partialUpdate(parede: PartialUpdateParede): Observable<EntityResponseType> {
    return this.http.patch<IParede>(`${this.resourceUrl}/${this.getParedeIdentifier(parede)}`, parede, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IParede>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IParede[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getParedeIdentifier(parede: Pick<IParede, 'id'>): number {
    return parede.id;
  }

  compareParede(o1: Pick<IParede, 'id'> | null, o2: Pick<IParede, 'id'> | null): boolean {
    return o1 && o2 ? this.getParedeIdentifier(o1) === this.getParedeIdentifier(o2) : o1 === o2;
  }

  addParedeToCollectionIfMissing<Type extends Pick<IParede, 'id'>>(
    paredeCollection: Type[],
    ...paredesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const paredes: Type[] = paredesToCheck.filter(isPresent);
    if (paredes.length > 0) {
      const paredeCollectionIdentifiers = paredeCollection.map(paredeItem => this.getParedeIdentifier(paredeItem)!);
      const paredesToAdd = paredes.filter(paredeItem => {
        const paredeIdentifier = this.getParedeIdentifier(paredeItem);
        if (paredeCollectionIdentifiers.includes(paredeIdentifier)) {
          return false;
        }
        paredeCollectionIdentifiers.push(paredeIdentifier);
        return true;
      });
      return [...paredesToAdd, ...paredeCollection];
    }
    return paredeCollection;
  }
}
