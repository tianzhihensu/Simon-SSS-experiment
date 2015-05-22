function node_info = initilizeNodes( num_node )
% INITILIZENODES 此处显示有关此函数的摘要
% function node_info = initilizeNodes( num_node ): generate a node_info
% matrix, num_node rows by 4 cols
% Input:
%       num_node: number of nodes
% Output:
%       node_info:  num_node rows by 4 cols
%       node_info(:, 1): the rand values under N(mu, sigma)
%       node_info(:, 2): the mu of this node delay
%       node_info(:, 3): the sigma of this node delay
%       node_info(:, 4): the normal value of node

source_data = load('node_mu_sigma.txt');
% After loading txt file, each node should has a unique serial number.
% So, each node's serial number must be identified before taking more
% actions.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% node/edge ---- serial number
% N3 ---- 1
% EA ---- 2
% EB ---- 3
% N3 ---- 4
% EC ---- 5
% ED ---- 6
% N2 ---- 7
% EE ---- 8
% EF ---- 9
% N4 ---- 10
%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% a node's delay obeys N(mu, sigma),
% without been monitored, a node obeys N(mu + normal, sigma).
% if equipped with a monitor, node obeys N(normal, 0),
% that is to say, the node hasn't delay, the value of this node is normal.
node_info_first = zeros(num_node, 1);   % set the values 0.
node_info_second = source_data(:, 2);   % get the mu list of delay, corresponding to sequence.
node_info_third = source_data(:, 3);    % get the sigma list of delay, corresponding to sequence.
node_info_fourth = source_data(:, 4);   % get the normal value of node, corresponding to sequence.

for i=1:num_node
    mu = node_info_second(i, 1);
    sigma = node_info_third(i, 1);
    normal = node_info_fourth(i, 1);    % in initilization period, the random values must take factor of node delay into consideration.
    node_info_first(i, 1) = normrnd(mu + normal, sigma, 1, 1);   % get the random values under Normal distribution
end

node_info = [node_info_first node_info_second node_info_third, node_info_fourth];
end

