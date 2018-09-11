package uk.nhs.careconnect.ri.daointerface;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.dstu3.model.*;
import uk.nhs.careconnect.fhir.OperationOutcomeException;
import uk.nhs.careconnect.ri.entity.schedule.ScheduleEntity;

import java.util.List;

public interface SlotRepository extends BaseRepository<SlotEntity,Slot> {
    void save(FhirContext ctx, SlotEntity location) throws OperationOutcomeException;

    Schedule read(FhirContext ctx, IdType theId);

    ScheduleEntity readEntity(FhirContext ctx, IdType theId);

    Schedule create(FhirContext ctx, Slot location, @IdParam IdType theId, @ConditionalUrlParam String theConditional) throws OperationOutcomeException;



    List<Schedule> searchSlot(FhirContext ctx,

                                                    @OptionalParam(name = Schedule.SP_IDENTIFIER) TokenParam identifier,
                                                    @OptionalParam(name = Schedule.SP_ACTOR) StringParam actor,
                                                    @OptionalParam(name = Schedule.SP_TYPE) TokenOrListParam codes,
                                                    @OptionalParam(name = Schedule.SP_RES_ID) TokenParam id
                                                    //@OptionalParam(name = Schedule.SP_ORGANIZATION) ReferenceParam organisation

    );

    List<ScheduleEntity> searchSlotEntity(FhirContext ctx,

                                                                @OptionalParam(name = Slot.SP_IDENTIFIER) TokenParam identifier,
                                                                @OptionalParam(name = Slot.SP_ACTOR) StringParam actor,
                                                                @OptionalParam(name = Slot.SP_TYPE) TokenOrListParam codes,
                                                                @OptionalParam(name = Slot.SP_RES_ID) TokenParam id
                                                                //@OptionalParam(name = Slot.SP_ORGANIZATION) ReferenceParam organisation

    );
}
