# Introduction #

Interval Tree Clocks can substitute both [Version vectors](http://en.wikipedia.org/wiki/Version_vector) and [Vector clocks](http://en.wikipedia.org/wiki/Vector_clock).

Version Vectors are used to track data dependency among replicas. They are used in replicated file systems (such as [Coda](http://en.wikipedia.org/wiki/Coda_(file_system))) and in Cloud engines (such as Amazon [Dynamo](http://en.wikipedia.org/wiki/Dynamo_(storage_system)) and Cassandra).

Vector Clocks track causality dependency between events in distributed processes. They are used in are used in group communication protocols (such as in the Spread toolkit), in consistent snapshots algorithms, etc.

ITCs can be used in all these settings and will excel in dynamic settings, i.e. whenever the number and set of active entities varies during the system execution, since it allows localized introduction and removal of entities.
Before ITCs, the typical strategy to address these dynamic settings was to implement the classical vectors as mappings from a globally unique id to an integer counter. The drawback is that unique ids are not space efficient and that if the active entities change over time (under churn) the state dedicated to the mapping will keep growing. This has lead to ad-hoc pruning solutions (e.g. in Dynamo) that can introduce errors and compromise causality tracking.

ITCs encode the state needed to track causality in a stamp, composed of an event and id component, and introduce 3 basic operations:

**Fork** is used to introduce new stamps. Allows the cloning of the causal past of a stamp, resulting in a pair of stamps that have identical copies of the event component and distinct ids. E.g. it can be used to introduce new replicas to a system.

**Join** is used to merge two stamps. Produces a new stamp that incorporates both causal pasts. E.g. it can be used to retire replicas or receive causal information from messages.

**Event** is used to add causal information to a stamp, "incrementing" the event component and keeping the id.

(**Peek** is a special case of fork that only copies the event component and creates a new stamp with a null id. It can be used to make messages that transport causal information.)

# Simulating Version Vectors #

First replicas need to be created. A seed stamp (with a special id component) is first created and the desired number of replicas can be created by forking this initial seed. Bellow we create 4 replicas (Java objects a,b,c,d - both in imperative and functional style):

```
Stamp a = new Stamp(); // Seed
Stamp b = a.fork();
Stamp c = a.fork();
//or
Stamp[] out = Stamp.fork(c);
c = out[0];
Stamp d = out[1];

```

(Notice that any stamp can be forked, here we forked stamp a twice and stamp c once.)

Since no events have been registered, these stamps all compare as equal. Since a stamp method leq (_less or equal_) is provided, stamps x and y are equivalent when both x.leq(y) and y.leq(x) are true.

Now, suppose that stamp b is associated to a ReplicaB and this replica was modified. We
note this by doing:

```
b.event();
//or
b = Stamp.event(b);
```

Now stamp b is greater than all the others. We can do the same in stamp d to denote an update on ReplicaD:

```
d=d.event();
//or
d = Stamp.event(d);
```

These two stamps are now concurrent. Thus b.leq(d) is false and d.leq(b) is also false.

Now suppose that we want to merge the updates in ReplicaB and ReplicaD. One way is to
create a replica that reflects both updates:

```
b.join(d);
//or
b = Stamp.join(b,d);
```

This stamp e will now have an id that joins the ids in b and d, and has an event component that holds both issued events.  An alternative way, that keeps the number of replicas/stamps and does not form new ids, is to exchange events between both replicas.

```
b.join(d.peek());
d.join(Stamp.peek(b));
```

Now, stamps b and d are no longer concurrent and will compare as equivalent, since they depict the same events.

# Simulating Vector Clocks #


# Use cases #